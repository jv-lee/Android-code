package com.lee.library.cache.impl;

import android.support.v4.util.LruCache;

/**
 * @author jv.lee
 * @date 2019-11-14
 * @description
 */
public class MemoryCache extends LruCache<String,String> {
    public MemoryCache(int maxSize) {
        super(maxSize);
    }
}
