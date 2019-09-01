package com.lee.code.glide.cache;

import android.graphics.Bitmap;

import com.lee.code.glide.request.BitmapRequest;


public interface BitmapCache {
    /**
     * 存入内存
     * @param request
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 读取缓存的图片
     * @param request
     */
    Bitmap get(BitmapRequest request);

    /**
     * 清除缓存的图片
     * @param request
     */
    void remove(BitmapRequest request);
//清除所属的activity 的bitmap
    public void remove(int activityCode);
}
