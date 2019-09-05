package com.lee.glide.cache;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.lee.glide.resource.Value;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 内存缓存 -- LRU算法
 */
public class MemoryCache extends LruCache<String, Value> {

    private boolean useRemove;

    private MemoryCacheCallback memoryCacheCallback;

    /**
     * 传入元素最大值
     *
     * @param maxSize 内存缓存元素最大值
     */
    public MemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Value value) {
        Bitmap bitmap = value.getBitmap();

        //API 1 开始 {int result = bitmap.getRowBytes() * bitmap.getHeight();}
        //API 12 3.0 内存复用上的区别（所属的） {bitmap.getByteCount();}
        //API 19 4.4 内存复用上的区别 （整个的）
        return bitmap.getAllocationByteCount();
    }

    /**
     * 移除条件
     * 1、重复的key
     * 2、最少使用的元素
     *
     * @param evicted
     * @param key
     * @param oldValue
     * @param newValue
     */
    @Override
    protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Value oldValue, @Nullable Value newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);

        //被动调用移除
        if (memoryCacheCallback != null && !useRemove) {
            memoryCacheCallback.entryRemovedMemoryCache(key, oldValue);
        }
    }

    /**
     * TODO 手动移除元素
     *
     * @param key 移除元素的key
     * @return 被移除的元素
     */
    public Value useRemove(String key) {
        //设置删除状态标识符
        useRemove = true;
        Value remove = remove(key);
        useRemove = false;
        return remove;
    }

    /**
     * TODO 设置被动移除元素 监听
     *
     * @param memoryCacheCallback 监听接口
     */
    public void setMemoryCacheCallback(MemoryCacheCallback memoryCacheCallback) {
        this.memoryCacheCallback = memoryCacheCallback;
    }

}
