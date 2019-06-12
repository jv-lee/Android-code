package com.lee.code.livepush.meida;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import com.lee.code.livepush.LivePusher;


/**
 * @author jv.lee
 */
public class VideoChannel2 implements Camera2Helper.OnSurfaceChange {
    private static final String TAG = "LEE>>>";
    private Camera2Helper cameraHelper;
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

    public VideoChannel2(LivePusher livePusher, Activity activity, int width, int height, int bitrate, int fps, int cameraId) {
        mLivePusher = livePusher;
        mBitrate = bitrate;
        mFps = fps;
        cameraHelper = new Camera2Helper(activity, cameraId, width, height);
        cameraHelper.setOnSurfaceChange(this);
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

    @Override
    public void onPreviewFrame(byte[] data) {
        if (isLiving) {
            Log.i(TAG, "传递一帧图像：" + data);
            mLivePusher.nativePushVideo(data);
        }
    }

    @Override
    public void onChanged(int w, int h) {
        mLivePusher.nativeSetVideoEncInfo(w, h, mFps, mBitrate);
    }
}
