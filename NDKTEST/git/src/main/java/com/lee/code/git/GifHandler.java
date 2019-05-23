package com.lee.code.git;

import android.graphics.Bitmap;

/**
 * @author jv.lee
 * @date 2019-05-23
 */
public class GifHandler {

    static{
        System.loadLibrary("native-lib");
    }

    private long gifAddress;

    public GifHandler(String path) {
        this.gifAddress = loadPath(path);
    }

    public int getWidth() {
        return getWidth(gifAddress);
    }

    public int getHeight(){
        return getHeight(gifAddress);
    }

    public int updateFrame(Bitmap bitmap) {
        return updateFrame(gifAddress, bitmap);
    }

    /**
     * 通过物理地址获取 图片native结构体地址
     * @param path
     * @return
     */
    private native long loadPath(String path);

    /**
     * 获取宽度 long类型的结构体
     * @param ndkGif
     * @return
     */
    public native int getWidth(long ndkGif);
    public native int getHeight(long ndkGif);
    public native int updateFrame(long ndkGif, Bitmap bitmap);
}
