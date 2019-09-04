package com.lee.glide.pool;

import android.graphics.Bitmap;

/**
 * @author jv.lee
 * @date 2019-09-04
 * @description 复用池 标准
 */
public interface BitmapPool {

    /**
     * 存入到复用池
     *
     * @param bitmap
     */
    void put(Bitmap bitmap);

    /**
     * 获取匹配可以复用的Bitmap
     *
     * @param w      宽
     * @param h      高
     * @param config 图片配置
     * @return Bitmap
     */
    Bitmap get(int w, int h, Bitmap.Config config);

}
