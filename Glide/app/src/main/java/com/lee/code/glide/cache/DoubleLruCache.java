package com.lee.code.glide.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.lee.code.glide.request.BitmapRequest;


public class DoubleLruCache implements BitmapCache {

    private static DoubleLruCache instance;

    public static DoubleLruCache getInstance(Context context) {
        if (instance == null) {
            synchronized (DoubleLruCache.class) {
                if (instance == null) {
                    instance = new DoubleLruCache(context);
                }
            }
        }
        return instance;
    }
    public static DoubleLruCache getInstance() {
        if (instance == null) {
            throw new RuntimeException("请在application初始化");
        }
        return instance;
    }
    private MemoryLruCache lruCache;

    private DiskBitmapCache bitmapCache;

    public DoubleLruCache(Context context){
        bitmapCache = DiskBitmapCache.getInstance(context);
        lruCache = MemoryLruCache.getInstance();
    }


    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        lruCache.put(request,bitmap);
        bitmapCache.put(request,bitmap);

    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = lruCache.get(request);
        if (bitmap == null){
            bitmap = bitmapCache.get(request);
            lruCache.put(request,bitmap);
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
        lruCache.remove(request);
        bitmapCache.remove(request);
    }

    @Override
    public void remove(int activityCode) {
        lruCache.remove(activityCode);
        bitmapCache.remove(activityCode);
    }


}
