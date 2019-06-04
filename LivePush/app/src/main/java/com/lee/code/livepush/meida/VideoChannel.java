package com.lee.code.livepush.meida;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.lee.code.livepush.LivePusher;


/**
 * @author jv.lee
 */
public class VideoChannel implements Camera.PreviewCallback, CameraHelper.OnChangedSizeListener {
    private static final String TAG = "LEE>>>";
    private CameraHelper cameraHelper;
    /**
     * 码率
     */
    private int mBitrate;
    /**
     * 帧率
     */
    private int mFps;
    private boolean isLiving;
    private LivePusher mLivePusher;

    public VideoChannel(LivePusher livePusher, Activity activity, int width, int height, int bitrate, int fps, int cameraId) {
        mLivePusher = livePusher;
        mBitrate = bitrate;
        mFps = fps;
        cameraHelper = new CameraHelper(activity, cameraId, width, height);
        cameraHelper.setPreviewCallback(this);
        cameraHelper.setOnChangedSizeListener(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isLiving) {
//            Log.i(TAG, "开启直播 获取到一帧   onPreviewFrame: ");
            mLivePusher.nativePushVideo(data);
        }

    }

    @Override
    public void onChanged(int w, int h) {
        mLivePusher.nativeSetVideoEncInfo(w, h, mFps, mBitrate);
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        cameraHelper.setPreviewDisplay(surfaceHolder);
    }

    public void startLive() {
        isLiving = true;
    }
}
