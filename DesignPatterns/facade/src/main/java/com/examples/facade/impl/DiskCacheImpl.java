package com.examples.facade.impl;

import android.graphics.Bitmap;

import com.examples.facade.thing.DiskCache;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class DiskCacheImpl implements DiskCache {
    @Override
    public Bitmap findByDisk(String url) {
        System.out.println("通过图片url，寻找本地文件中缓存图片");
        return null;
    }
}
