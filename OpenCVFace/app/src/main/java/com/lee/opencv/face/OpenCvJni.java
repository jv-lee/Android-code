package com.lee.opencv.face;

import android.view.Surface;

/**
 * @author jv.lee
 * @date 2019/6/25.
 * @description
 */
public class OpenCvJni {
    static {
        System.loadLibrary("native-lib");
    }

    public native void init(String path);

    public native void postData(byte[] data, int width, int height, int cameraId);

    public native void setSurface(Surface surface);
}
