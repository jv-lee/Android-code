package com.lee.code.livepush;

import android.app.Activity;
import android.view.SurfaceHolder;
import com.lee.code.livepush.meida.AudioChannel;
import com.lee.code.livepush.meida.VideoChannel;
import com.lee.code.livepush.meida.VideoChannel2;


/**
 * @author jv.lee
 */
public class LivePusher {
    private AudioChannel audioChannel;
    private VideoChannel2 videoChannel;

    static {
        System.loadLibrary("native-lib");
    }

    LivePusher(Activity activity, int width, int height, int bitrate,
               int fps, int cameraId) {
        nativeInit();
        videoChannel = new VideoChannel2(this, activity, width, height, bitrate, fps, cameraId);
        audioChannel = new AudioChannel(this);
    }

    void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        videoChannel.setPreviewDisplay(surfaceHolder);
    }

    void switchCamera() {
        videoChannel.switchCamera();
    }

    void startLive(String path) {
        nativeStart(path);
        videoChannel.startLive();
        audioChannel.startLive();
    }

    public native void nativeInit();

    public native void nativeSetVideoEncInfo(int width, int height, int fps, int bitrate);

    public native void nativeStart(String path);

    public native void nativePushVideo(byte[] data);

    public native void nativePushAudio(byte[] bytes);

    public native void nativeSetAudioEncInfo(int sampleRateInHz, int channels);


    public native int getInputSamples();
}
