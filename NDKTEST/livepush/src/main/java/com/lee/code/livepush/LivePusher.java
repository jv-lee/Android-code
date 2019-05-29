package com.lee.code.livepush;

import android.app.Activity;
import android.view.SurfaceHolder;
import com.lee.code.livepush.meida.AudioChannel;
import com.lee.code.livepush.meida.VideoChannel;

/**
 * @author jv.lee
 * @date 2019-05-30
 */
public class LivePusher {
    private AudioChannel audioChannel;
    private VideoChannel videoChannel;

    static{
        System.loadLibrary("native-lib");
    }

    public LivePusher(Activity activity, int width, int height, int bitrate, int fps, int cameraId) {
        videoChannel = new VideoChannel();
        audioChannel = new AudioChannel();
    }

    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {

    }

    public void switchCamera(){

    }

}
