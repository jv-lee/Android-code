package com.lee.opencv.face.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static com.lee.opencv.face.utils.ImageUtil.NV21;


/**
 * @author jv.lee
 * @date 2019/6/3.
 * description：
 */
public class Camera2Helper implements SurfaceHolder.Callback, ImageReader.OnImageAvailableListener {

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

    private SurfaceHolder mSurfaceHolder;
    private CameraManager mCameraManager;
    private Handler childHandler, mainHandler;
    private ImageReader mImageReaderJPG;
    private ImageReader mImageReaderPreview;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
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
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(mActivity.getMainLooper());
        mImageReaderPreview = ImageReader.newInstance(mWidth, mHeight, ImageFormat.YUV_420_888, 1);
        mImageReaderPreview.setOnImageAvailableListener(this, null);
        mCameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
    }

    public void startPreview() {
        try {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开摄像头
            mCameraManager.openCamera(String.valueOf(mCameraId), mStateCallback, mainHandler);
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

    @Override
    public void onImageAvailable(ImageReader reader) {
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
        byte[] yuv420pData = ImageUtil.getBytesFromImageAsType(image, NV21);

        System.arraycopy(Objects.requireNonNull(yuv420pData), 0, yuv420pbuf, 0, n_image_size);
        if (onSurfaceChange != null) {
            onSurfaceChange.onPreviewFrame(yuv420pData);
        }
        image.close();
    }

    private void readPreview() {

        try {
            // 创建预览需要的CaptureRequest.Builder
            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            previewRequestBuilder.addTarget(mImageReaderPreview.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReaderPreview.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) {
                        return;
                    }
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
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
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private OnSurfaceChange onSurfaceChange;

    public void setOnSurfaceChange(OnSurfaceChange onSurfaceChange) {
        this.onSurfaceChange = onSurfaceChange;
    }

    public interface OnSurfaceChange {
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
