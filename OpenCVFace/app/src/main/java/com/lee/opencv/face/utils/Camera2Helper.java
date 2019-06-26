package com.lee.opencv.face.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static com.lee.opencv.face.utils.ImageUtil.YUV420P;


/**
 * @author jv.lee
 * @date 2019/6/3.
 * description：
 */
public class Camera2Helper implements SurfaceHolder.Callback {

    private static final String TAG = "LEE>>>";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    //使图像竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Activity mActivity;
    private int mCameraId;
    private int mWidth;
    private int mHeight;
    private boolean taskPictureEnable;

    private SurfaceHolder mSurfaceHolder;
    private CameraManager mCameraManager;
    private HandlerThread handlerThread;
    private Handler childHandler, mainHandler;
    private ImageReader mImageReaderJPG;
    private ImageReader mImageReaderPreview;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    public Camera2Helper(Activity activity, int cameraId, int width, int height) {
        mActivity = activity;
        mCameraId = cameraId;
        mWidth = width;
        mHeight = height;
    }

    public void switchCamera() {
        if (mCameraId == CameraCharacteristics.LENS_FACING_FRONT) {
            mCameraId = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            mCameraId = CameraCharacteristics.LENS_FACING_FRONT;
        }
        stopPreview();
        startPreview();
    }

    public int getCameraId() {
        return mCameraId;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    private void initPreview() {
        handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(mActivity.getMainLooper());
        //创建照片预览
        mImageReaderJPG = ImageReader.newInstance(mWidth, mHeight, ImageFormat.JPEG, 1);
        mImageReaderJPG.setOnImageAvailableListener(imageListener, mainHandler);
        //创建预览数据回调
        mImageReaderPreview = ImageReader.newInstance(mWidth, mHeight, ImageFormat.YUV_420_888, 1);
        mImageReaderPreview.setOnImageAvailableListener(previewListener, null);

        mCameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
    }

    public void startPreview() {
        try {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开摄像头  摄像头id、相机状态回调、指定callBack回调在哪个线程 , null为当前线程
            mCameraManager.openCamera(String.valueOf(mCameraId), deviceStateCall, mainHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        stopPreview();
        startPreview();
        if (onSurfaceChange != null) {
            onSurfaceChange.onChanged(holder, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
    }

    private void readPreview() {
        try {
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求  输出surface列表、回调状态接口、回调接口运行在子线程
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReaderPreview.getSurface()), sessionStateCall, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听相机打开关闭
     */
    private CameraDevice.StateCallback deviceStateCall = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            readPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            stopPreview();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Toast.makeText(mActivity, "开启摄像头失败", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 预览照片配置
     */
    private CameraCaptureSession.StateCallback sessionStateCall = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            if (null == mCameraDevice) {
                return;
            }
            // 当摄像头已经准备好时，开始显示预览
            mCameraCaptureSession = cameraCaptureSession;
            try {
                // 创建预览需要的CaptureRequest.Builder
                CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                // 将SurfaceView的surface作为CaptureRequest.Builder的目标
                previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
                previewRequestBuilder.addTarget(mImageReaderPreview.getSurface());
                // 自动对焦
                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // 打开闪光灯
                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                // 获取手机方向
                int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
                // 根据设备方向计算设置预览图像的方向
                previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                // 显示预览
                CaptureRequest previewRequest = previewRequestBuilder.build();
                mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            Toast.makeText(mActivity, "配置失败", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 预览数据回调
     */
    private ImageReader.OnImageAvailableListener previewListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            if (taskPictureEnable) {
                taskPictureEnable = false;
                saveImage(reader);
                return;
            }

            byte[] data = buildImageData(reader);
            if (onSurfaceChange != null) {
                onSurfaceChange.onPreviewFrame(data);
            }
        }
    };

    /**
     * 照片成像回调
     */
    private ImageReader.OnImageAvailableListener imageListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            saveImage(reader);
        }
    };

    /**
     * 拍照
     */
    public void takePicture() {
        if (mCameraDevice == null) {
            return;
        }
        taskPictureEnable = true;
        if (taskPictureEnable) {
            return;
        }
        // 创建拍照需要的CaptureRequest.Builder
        CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReaderPreview.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储一帧图像
     *
     * @param reader
     */
    private void saveImage(ImageReader reader) {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mActivity, "你的sd卡不可用。", Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取捕获的照片数据
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        //手机拍照都是存到这个路径
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
        String picturePath = System.currentTimeMillis() + ".jpg";
        File file = new File(filePath, picturePath);
        try {
            //存到本地相册
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.close();
            Toast.makeText(mActivity, "成功拍照", Toast.LENGTH_SHORT).show();
            //显示图片
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 2;
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }

    /**
     * 获取图像原始数据数组
     *
     * @param reader
     * @return
     */
    private byte[] buildImageData(ImageReader reader) {
        // 获取捕获的照片数据
        Image image = reader.acquireNextImage();
        // 从image里获取三个plane
        Image.Plane[] planes = image.getPlanes();

        for (int i = 0; i < planes.length; i++) {
            ByteBuffer iBuffer = planes[i].getBuffer();
            int iSize = iBuffer.remaining();
        }
        int n_image_size = image.getWidth() * image.getHeight() * 3 / 2;
        final byte[] yuv420pbuf = new byte[n_image_size];
        byte[] yuv420pData = ImageUtil.getBytesFromImageAsType(image, YUV420P);

        System.arraycopy(Objects.requireNonNull(yuv420pData), 0, yuv420pbuf, 0, n_image_size);
        image.close();
        return yuv420pData;
    }


    public void releaseCamera() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (mImageReaderPreview != null) {
            mImageReaderPreview.close();
            mImageReaderPreview = null;
        }

        if (mImageReaderJPG != null) {
            mImageReaderJPG.close();
            mImageReaderJPG = null;
        }
    }

    public void releaseThread() {
        handlerThread.quitSafely();
    }

    private SurfaceChange onSurfaceChange;

    public void setOnSurfaceChange(SurfaceChange onSurfaceChange) {
        this.onSurfaceChange = onSurfaceChange;
    }

    public interface SurfaceChange {
        /**
         * 获取每一帧NV21图像数组
         *
         * @param data
         */
        void onPreviewFrame(byte[] data);

        /**
         * surface重绘时
         *
         * @param holder
         * @param w
         * @param h
         */
        void onChanged(SurfaceHolder holder, int w, int h);
    }
}
