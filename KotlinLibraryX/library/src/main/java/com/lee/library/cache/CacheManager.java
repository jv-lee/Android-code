package com.lee.library.cache;

import android.content.Context;
import android.text.TextUtils;
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

    /**
     * 磁盘缓存开关 默认打开.
     */
    private static boolean isDisk = true;

    private MemoryCache memoryCache;
    private DiskCache diskCache;
    private Gson gson;

    private CacheManager() {
        gson = new Gson();
        memoryCache = new MemoryCache((int) MAX_SIZE);
        //私有目录无需申请权限
        diskCache = new DiskCache(mContext.getFilesDir().getAbsolutePath() + File.separator + DISK_LRU_CACHE_DIR, APP_VERSION, VALUE_COUNT, MAX_SIZE);
    }

    public synchronized static void init(Context context, int version) {
        APP_VERSION = version;
        mContext = context.getApplicationContext();
        if (instance == null) {
            instance = new CacheManager();
        }
    }

    public synchronized static CacheManager getDefault() {
        isDisk = true;
        if (instance == null) {
            throw new RuntimeException("未初始化context 和 具体版本");
        }
        return instance;
    }

    public synchronized static CacheManager getMemory() {
        isDisk = false;
        if (instance == null) {
            throw new RuntimeException("未初始化context 和 具体版本");
        }
        return instance;
    }

    /**
     * @param key   [a-zA-Z0-9+_/-]{1,120}
     * @param clazz 具体类型
     * @param <T>   泛型
     * @return 具体类型数据实体
     */
    public synchronized <T> T get(String key, Class<T> clazz) {
        try {
            String data = memoryCache.get(key);
            if (null != data) {
                Log.i(TAG, "get: from memory cacheData");
                return readJsonToObject(data, clazz);
            }

            if (!isDisk) return null;

            data = diskCache.get(key);
            if (null != data) {
                memoryCache.put(key, data);
                Log.i(TAG, "get: from disk cacheData");
                return readJsonToObject(data, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "get: local not cache, request network data.");
        return null;
    }

    /**
     * @param key  [a-zA-Z0-9+_/-]{1,120}
     * @param type 具体类型
     * @param <T>  泛型
     * @return 具体类型数据实体
     */
    public synchronized <T> T get(String key, Type type) {
        try {
            String data = memoryCache.get(key);
            if (null != data) {
                Log.i(TAG, "get: from memory cacheData");
                return readJsonToObject(data, type);
            }

            if (!isDisk) return null;

            data = diskCache.get(key);
            if (null != data) {
                memoryCache.put(key, data);
                Log.i(TAG, "get: from disk cacheData");
                return readJsonToObject(data, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "get: local not cache, request network data.");
        return null;
    }

    /**
     * @param key   [a-zA-Z0-9+_/-]{1,120}
     * @param value 具体对象数据
     * @param <T>   泛型
     */
    public synchronized <T> void put(String key, T value) {
        String json = readObjectToJson(value);
        memoryCache.put(key, json);
        if (isDisk) {
            diskCache.put(key, json);
        }
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
