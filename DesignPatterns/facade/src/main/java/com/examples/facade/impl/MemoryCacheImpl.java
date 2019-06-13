package com.examples.facade.impl;

import android.graphics.Bitmap;

import com.examples.facade.thing.MemoryCache;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class MemoryCacheImpl implements MemoryCache {
    @Override
    public Bitmap findByMemory(String url) {
        System.out.println("听过图片url 寻找内存中缓存图片");
        return null;
    }
}
