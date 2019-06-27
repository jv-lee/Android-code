package com.lee.open.photo.utils;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

/**
 * @author jv.lee
 */
public class CameraHelper implements Camera.PreviewCallback {

    private static final String TAG = "CameraHelper";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private Camera.PreviewCallback mPreviewCallback;
    private SurfaceTexture mSurfaceTexture;

    public CameraHelper(int cameraId) {
        mCameraId = cameraId;
    }

    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopPreview();
        startPreview(mSurfaceTexture);
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void startPreview(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        try {
            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setPreviewSize(WIDTH, HEIGHT);
            mCamera.setParameters(parameters);
            buffer = new byte[WIDTH * HEIGHT * 3 / 2];

            //数据缓存区
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            //设置预览画面
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != mPreviewCallback) {
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        camera.addCallbackBuffer(buffer);
    }
}
