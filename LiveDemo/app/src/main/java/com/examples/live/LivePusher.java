package com.examples.live;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.examples.live.meida.AudioChannel;
import com.examples.live.meida.VideoChannel;

/**
 * @author jv.lee
 * @date 2019/6/3.
 * description：
 */
public class LivePusher {

    private VideoChannel mVideoChannel;
    private AudioChannel mAudioChannel;

    static {
        System.loadLibrary("native-lib");
    }

    LivePusher(Activity activity, int width, int height, int bitrate, int fps, int cameraID) {
        nativeInit();
        mVideoChannel = new VideoChannel(this,activity,width,height,bitrate,fps,cameraID);
        mAudioChannel = new AudioChannel(this);
    }

    void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        mVideoChannel.setPreviewDisplay(surfaceHolder);
    }

    void switchCamera(){
        mVideoChannel.switchCamera();
    }

    void startLive(String path) {
        nativeStart(path);
        mVideoChannel.startLive();
    }

    public native void nativeInit();

    public native void nativeSetVideoEncInfo(int width, int height, int bitrate, int fps);

    public native void nativeStart(String path);

    public native void nativePushVideo(byte[] data);

}
