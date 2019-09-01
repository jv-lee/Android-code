package com.lee.glide.cache;

import com.lee.glide.resource.Value;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 内存缓存 元素移除监听回调接口
 */
public interface MemoryCacheCallback {

    /**
     * 内存缓存中移除的 key - value
     * @param key
     * @param oldValue
     */
    void entryRemovedMemoryCache(String key, Value oldValue);

}
