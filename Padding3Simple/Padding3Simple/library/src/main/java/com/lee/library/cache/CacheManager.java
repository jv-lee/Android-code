package com.lee.library.cache;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lee.library.cache.impl.DiskCache;
import com.lee.library.cache.impl.MemoryCache;

import java.io.File;
import java.lang.reflect.Type;

/**
 * @author jv.lee
 * @date 2019-11-14
 * @description 缓存管理类
 */
public class CacheManager {

    private static final String TAG = "CacheManager";

    private volatile static CacheManager instance;

    private static Context mContext;

    /**
     * 磁盘存储地址
     */
    private static final String DISK_LRU_CACHE_DIR = "disk_lru_cache_dir";

    /**
     * app版本，每次只缓存当前版本，修改后之前的缓存失效
     */
    private static int APP_VERSION = 1;

    /**
     * 通常情况下count为1
     */
    private static final int VALUE_COUNT = 1;

    /**
     * 最大缓存大小 可动态设置
     */
    private static final long MAX_SIZE = 1024 * 1024 * 100;

    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private Gson gson;

    private CacheManager() {
        gson = new Gson();
        memoryCache = new MemoryCache((int) MAX_SIZE);
        //私有目录无需申请权限
        diskCache = new DiskCache(mContext.getFilesDir().getAbsolutePath() + File.separator + DISK_LRU_CACHE_DIR, APP_VERSION, VALUE_COUNT, MAX_SIZE);
    }

    public synchronized static void getInstance(Context context, int version) {
        APP_VERSION = version;
        mContext = context.getApplicationContext();
        if (instance == null) {
            instance = new CacheManager();
        }
    }

    public synchronized static CacheManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("未初始化context 和 具体版本");
        }
        return instance;
    }

    /**
     * @param key   [a-z0-9_-]{1,120} 无法使用大写
     * @param clazz 具体类型
     * @param <T>   泛型
     * @return 具体类型数据实体
     */
    public synchronized <T> T get(String key, Class<T> clazz) {
        String data = memoryCache.get(key);
        if (null != data) {
            Log.i(TAG, "get: from memory cacheData" );
            return readJsonToObject(data, clazz);
        }

        data = diskCache.get(key);
        if (null != data) {
            memoryCache.put(key, data);
            Log.i(TAG, "get: from disk cacheData");
            return readJsonToObject(data, clazz);
        }
        Log.i(TAG, "get: local not cache, request network data.");
        return null;
    }

    /**
     * @param key   [a-z0-9_-]{1,120} 无法使用大写
     * @param type 具体类型
     * @param <T>   泛型
     * @return 具体类型数据实体
     */
    public synchronized <T> T get(String key, Type type) {
        String data = memoryCache.get(key);
        if (null != data) {
            Log.i(TAG, "get: from memory cacheData");
            return readJsonToObject(data, type);
        }

        data = diskCache.get(key);
        if (null != data) {
            memoryCache.put(key, data);
            Log.i(TAG, "get: from disk cacheData");
            return readJsonToObject(data, type);
        }
        Log.i(TAG, "get: local not cache, request network data.");
        return null;
    }

    /**
     * @param key   [a-z0-9_-]{1,120} 无法使用大写
     * @param value 具体对象数据
     * @param <T>   泛型
     */
    public synchronized  <T> void put(String key, T value) {
        String json = readObjectToJson(value);
        memoryCache.put(key, json);
        diskCache.put(key, json);
    }

    private <T> String readObjectToJson(T value) {
        return gson.toJson(value);
    }

    private <T> T readJsonToObject(String value, Class<T> clazz) {
        return gson.fromJson(value, clazz);
    }

    private <T> T readJsonToObject(String value, Type type) {
        return gson.fromJson(value, type);
    }

}
