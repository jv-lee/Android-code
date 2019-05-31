package com.lee.code.livepush;

import android.app.Activity;
import android.view.SurfaceHolder;
import com.lee.code.livepush.meida.AudioChannel;
import com.lee.code.livepush.meida.VideoChannel;


public class LivePusher {
    private AudioChannel audioChannel;
    private VideoChannel videoChannel;
    static {
        System.loadLibrary("native-lib");
    }

    public LivePusher(Activity activity, int width, int height, int bitrate,
                      int fps, int cameraId) {
        nativeInit();
        videoChannel = new VideoChannel(this, activity, width, height, bitrate, fps, cameraId);
        audioChannel = new AudioChannel(this);


    }
    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        videoChannel.setPreviewDisplay(surfaceHolder);
    }
    public void switchCamera() {
        videoChannel.switchCamera();
    }

    public void startLive(String path) {
        nativeStart(path);
        videoChannel.startLive();
    }

    public native void nativeInit();

    public native void nativeSetVideoEncInfo(int width,int height,int fps,int bitrate);

    public native void nativeStart(String path);

    public native void nativePushVideo(byte[] data);


}
