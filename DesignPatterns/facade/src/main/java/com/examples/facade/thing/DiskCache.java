package com.examples.facade.thing;

import android.graphics.Bitmap;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public interface DiskCache {

    /**
     * 从本地获取图片
     *
     * @param url 地址
     * @return bitmap
     */
    Bitmap findByDisk(String url);
}
