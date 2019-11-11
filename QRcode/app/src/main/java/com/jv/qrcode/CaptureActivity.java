package com.jv.qrcode;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.Result;
import com.jv.library.CaptureCallback;
import com.jv.library.camera.CameraManager;
import com.jv.library.decode.DecodeThread;
import com.jv.qrcode.utils.BeepManager;
import com.jv.library.utils.CaptureActivityHandler;
import com.jv.library.utils.InactivityTimer;
import com.jv.qrcode.utils.Utils;

import java.io.IOException;

public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback, CaptureCallback {

    private Context context;//上下文
    private Activity activity;
    private TextView tvLight;// 开灯/关灯
    private SurfaceView scanPreview; // SurfaceView控件
    private RelativeLayout scanContainer; // 布局容器
    private RelativeLayout scanCropView; // 布局中的扫描框

    private boolean isPause; // 是否暂停
    private CaptureActivityHandler handler;
    private Rect mCropRect; //矩形
    private CameraManager cameraManager;//相机管理类
    private InactivityTimer inactivityTimer; //计时器
    private BeepManager beepManager; // 声音管理器
    private ObjectAnimator mObjectAnimator; //屬性動畫
    private boolean isHasSurface; // SurfaceView控件是否存在 ，surfaceCreated

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //保持屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        context = this;
        activity = this;

        init();
        initScan();
    }

    private void init() {
        tvLight = findViewById(R.id.tv_light);
        ToggleButton tbLight = findViewById(R.id.tb_light);
        //闪光灯控制
        tbLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvLight.setText("关灯");
                    Utils.openFlashlight(cameraManager);
                } else {
                    tvLight.setText("开灯");
                    Utils.closeFlashlight();
                }
            }
        });

        //打开相册
        findViewById(R.id.ll_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相册，做权限判断 ，权限验证
                Utils.openAlbum(activity);
            }
        });
    }

    //扫码初始化
    private void initScan() {
        scanPreview = findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        ImageView scanline = findViewById(R.id.scan_line);

        //扫描线性动画(属性动画可暂停)  设置为从Y坐标开始  :translationY 平移动画Y  上下移动  170dp的距离来回
        float curTranslationY = scanline.getTranslationY();
        mObjectAnimator = ObjectAnimator.ofFloat(scanline, "translationY", curTranslationY, Utils.dp2px(this, 170));
        //动画持续时间
        mObjectAnimator.setDuration(4000);
        //设置 线性动画匀速  Interpolator
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        //设置动画重复次数 死循环
        mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        //设置动画如何重复，从下到上，还是重新开始 从上到下  无限重复 上下开始
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScan();
    }


    @Override
    protected void onPause() {
        pauseScan();
        super.onPause();

    }

    //开始扫描
    private void startScan() {
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        if (isPause) {
            //如果是暂停，扫描动画暂停
            mObjectAnimator.resume();
            isPause = false;
        } else {
            mObjectAnimator.start();
        }
        //初始化相机管理器
        cameraManager = new CameraManager(this);
        handler = null;//重置一个handler
        if (isHasSurface) {
            initCamera(scanPreview.getHolder());
        } else {
            //等待surfaceCreated来初始化相机
            scanPreview.getHolder().addCallback(this);
        }
        //开启计时器
        inactivityTimer.onResume();
    }

    //暂停扫描
    private void pauseScan() {
        if (handler != null) {
            //handler退出同步并置空
            handler.quitSynchronously();
            handler = null;
        }
        //计时器暂停
        inactivityTimer.onPause();
        //关闭声音管理器
        beepManager.close();
        //关闭相机管理器的驱动
        cameraManager.closeDriver();
        if (!isHasSurface) {
            //remove
            scanPreview.getHolder().removeCallback(this);
        }
        //动画暂停
        mObjectAnimator.pause();
        isPause = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e("QRcode", "SurfaceHolder is null");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public Rect getCropRect() {
        return mCropRect;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void handleDecode(Result result, Bundle bundle) {
        //解码 扫码成功后回调的方法
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate(); //播放声音提示

        //将扫码的结果返回到MainActivity
        Intent intent = new Intent();
        intent.putExtra(Utils.BAR_CODE, result.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

    //初始化相机
    private void initCamera(SurfaceHolder holder) {
        if (holder == null) {
            throw new IllegalStateException("SurfaceHolder is null");
        }
        if (cameraManager.isOpen()) {
            Log.e("QRcode >>> ", "camer is open");
            return;
        }
        try {
            cameraManager.openDriver(holder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }
            initCrop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始化截取的矩形区域
    private void initCrop() {
        //获取相机的宽高
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHieght = cameraManager.getCameraResolution().x;

        //获取布局中扫描框的位置信息
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - Utils.getStatusBarHeight(context);

        //获取截取的宽高
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        //获取布局容器的宽高
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        //计算最终截取的矩形的左上角顶点x坐标 y坐标
        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHieght / containerHeight;

        //计算最终截取的矩形宽 度 高
        int width = cropWidth * cameraWidth / containerHeight;
        int height = cropHeight * cameraHieght / containerHeight;

        //生成最终的截取的矩形
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册返回
        if (requestCode == Utils.SELECT_PIC_KITKAT && resultCode == Activity.RESULT_OK) {//4.4及以上图库
            Uri uri = data.getData();
            String path = Utils.getPath(context, uri);
            Result result = Utils.scanningImage(path);
            if (result == null) {
                Toast.makeText(context, "未发现二维码/条形码", Toast.LENGTH_SHORT).show();
            }else{
                String recode = Utils.recode(result.toString());
                Toast.makeText(context, recode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        //关闭计时器
        inactivityTimer.shutdown();
        if (mObjectAnimator != null) {
            //结束一个动画
            mObjectAnimator.end();
        }
        super.onDestroy();
    }
}
