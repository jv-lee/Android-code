package com.lee.code.imagecompress;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lee.library.imagecompress.config.CompressConfig;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
        CompressConfig.builder()
                .setUnCompressMinPixel(1000)//最小像素不压缩
                .setUnCompressNormalPixel(2000)//标准像素不压缩
                .setMaxPixel(1200)//长或宽不超过某像素
                .setMaxSize(200*1024)//压缩最大值
                .setEnablePixelCompress(true)//像素压缩
                .setEnableQualityCompress(true)//质量压缩
                .setEnableReserveRaw(true)//保留源文件
                .setCacheDir("")//没有设置的
                .setShowCompressDialog(true)//显示dialog
                .create();
//        testLuban();
    }

    private void testLuban() {
//        String mCacheDir =
//                Luban.with(this)
//                        .load("")//源文件路径
//                        .ignoreBy(100) //忽略100kb以内的图片不压缩
//                        .setTargetDir(getDatabasePath(""))//压缩后存放的地址
//                        .filter(new CompressionPredicate() {
//                            @Override
//                            public boolean apply(String path) {
//                                return false;
//                            }
//                        }).setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        Log.e("lee >>> ", "start");
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        Log.e("lee >>> ", file.getAbsolutePath());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("lee >>> ", e.getMessage());
//                    }
//                }).launch();
    }

    public void show(View view) {
        testLuban();
    }
}
