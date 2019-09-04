package com.lee.glide.disk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.lee.glide.Tool;
import com.lee.glide.pool.BitmapPool;
import com.lee.glide.resource.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private final int APP_VERSION = 1;

    /**
     * 通常情况下count为1
     */
    private final int VALUE_COUNT = 1;

    /**
     * 最大缓存大小 可动态设置
     */
    private final long MAX_SIZE = 1024 * 1024 * 10;

    /**
     * 磁盘缓存
     */
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
        //{Tool.checkNotEmpty(key)}
        DiskLruCache.Editor edit = null;
        OutputStream os = null;
        try {
            edit = diskLruCache.edit(key);
            //index不能大于VALUE_COUNT
            os = edit.newOutputStream(0);
            Bitmap bitmap = value.getBitmap();
            //将bitmap写入到outputStream中去
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (edit != null) {
                    edit.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (edit != null) {
                    edit.commit();
                }
                diskLruCache.flush();
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Value get(String key) {
        //{Tool.checkNotEmpty(key)}
        DiskLruCache.Snapshot snapshot = null;
        InputStream is = null;
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                is = snapshot.getInputStream(0);
                Value value = Value.getInstance();

//                BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
//                sizeOptions.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(is, null, sizeOptions);
//                int w = sizeOptions.outWidth;
//                int h = sizeOptions.outHeight;
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inBitmap = bitmapPool.get(w, h, Bitmap.Config.ARGB_8888);
//                options.inMutable = true;
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                options.inJustDecodeBounds = false;
//                options.inSampleSize = Tool.sampleBitmapSize(options, w, h);
//                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                value.setBitmap(bitmap);
                value.setKey(key);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (snapshot != null) {
                    snapshot.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
