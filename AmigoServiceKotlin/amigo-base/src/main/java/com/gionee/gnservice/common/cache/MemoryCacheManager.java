package com.gionee.gnservice.common.cache;

import android.annotation.SuppressLint;

import java.util.HashMap;

/**
 * Created by borney on 3/1/17.
 */
@SuppressLint("NewApi")
class MemoryCacheManager implements Cache {
    private HashMap<String, byte[]> bytesMap;

    MemoryCacheManager() {
        bytesMap = new HashMap<>();
    }

    @Override
    public <T> void putByteMapper(String key, T obj, ByteMapper<T> mapper) {
        bytesMap.put(key, mapper.getBytes(obj));
    }

    @Override
    public <T> T getByteMapper(String key, ByteMapper<T> mapper) {
        if (bytesMap.containsKey(key)) {
            return mapper.getObject(bytesMap.get(key));
        }
        return null;
    }

    @Override
    public boolean isExpired(String key) {
        throw new UnsupportedOperationException("not support isExpired(key, age) operation!!!");
    }

    @Override
    public boolean isExpired(String key, long age) {
        throw new UnsupportedOperationException("not support isExpired(key, age) operation!!!");
    }

    @Override
    public void evict(String key) {
        if (bytesMap.containsKey(key)) {
            bytesMap.remove(key);
        }
    }

    @Override
    public void evictAll() {
        bytesMap.clear();
    }

    @Override
    public boolean isCached(String key) {
        return bytesMap.containsKey(key);
    }
}
