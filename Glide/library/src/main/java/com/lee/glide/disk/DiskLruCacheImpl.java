package com.lee.glide.disk;

import android.os.Environment;

import com.lee.glide.resource.Value;

import java.io.File;
import java.io.IOException;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 磁盘缓存自身封装类
 */
public class DiskLruCacheImpl {

    /**
     * 磁盘存储地址
     */
    private final String DISK_LRU_CACHE_DIR = "disk_lru_cache_dir";

    /**
     * app版本，每次只缓存当前版本，修改后之前的缓存失效
     */
    private final int APP_VERSION = 999;

    /**
     * 通常情况下count为1
     */
    private final int VALUE_COUNT = 1;

    /**
     * 最大缓存大小
     */
    private final int MAX_SIZE = 1024 * 1024 * 10;

    private DiskLruCache diskLruCache;

    public DiskLruCacheImpl() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + DISK_LRU_CACHE_DIR);
            diskLruCache = DiskLruCache.open(file, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, Value value) {
        
    }

}
