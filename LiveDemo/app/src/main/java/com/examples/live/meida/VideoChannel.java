package com.examples.live.meida;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.examples.live.LivePusher;

/**
 * @author jv.lee
 * @date 2019/6/3.
 * description：
 */
public class VideoChannel implements Camera.PreviewCallback, CameraHelper.OnChangedSizeListener {
    private static final String TAG = "LEE>>>";
    private CameraHelper mCameraHelper;

    /**
     * 码率
     */
    private int mBitrate;

    /**
     * 帧率
     */
    private int mFps;
    private boolean isLiveing;
    private LivePusher mLivePusher;

    public VideoChannel(LivePusher livePusher, Activity activity, int width, int height, int bitrate, int fps, int cameraID) {
        mLivePusher = livePusher;
        mBitrate = bitrate;
        mFps = fps;
        mCameraHelper = new CameraHelper(activity, cameraID, width, height);
        mCameraHelper.setPreviewCallback(this);
        mCameraHelper.setOnChangedSizeListener(this);
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        mCameraHelper.setPreviewDisplay(surfaceHolder);
    }

    public void switchCamera() {
        mCameraHelper.switchCamera();
    }

    public void startLive() {
        isLiveing = true;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isLiveing) {
            mLivePusher.nativePushVideo(data);
        }
    }

    @Override
    public void onChanged(int w, int h) {
        mLivePusher.nativeSetVideoEncInfo(w, h, mBitrate, mFps);
    }
}
