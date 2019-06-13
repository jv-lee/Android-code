package com.examples.facade.thing;

import android.graphics.Bitmap;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public interface MemoryCache {

    /**
     * 从内存中寻找缓存图片
     *
     * @param url 地址
     * @return bitmap
     */
    Bitmap findByMemory(String url);
}
