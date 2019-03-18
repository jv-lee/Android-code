package com.lee.code.glide.cache;

import android.graphics.Bitmap;
import android.util.LruCache;


import com.lee.code.glide.request.BitmapRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryLruCache implements BitmapCache {

    private LruCache<String,Bitmap> lruCache;
    private HashMap<String, Integer> activityCache;
    private static volatile MemoryLruCache instance;

    private static final byte[]lock = new byte[0];

    public static MemoryLruCache getInstance(){
        if (instance == null){
            synchronized (lock){
                if (instance == null){
                    instance = new MemoryLruCache();
                }
            }
        }
        return instance;
    }

    private MemoryLruCache(){

           int maxMemorySize = 1024*1024*1024;

        lruCache = new LruCache<String, Bitmap>(maxMemorySize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //一张图片的大小
                return value.getRowBytes()*value.getHeight();
            }
        };
        activityCache = new HashMap<>();
    }


    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (bitmap != null){
            lruCache.put(request.getUriMD5(),bitmap);
            activityCache.put(request.getUriMD5(), request.getContext().hashCode());
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return lruCache.get(request.getUriMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        lruCache.remove(request.getUriMD5());
    }
    @Override
    public void remove(int activity) {
        List<String> tempUriMd5List = new ArrayList<>();
        for (String uriMd5 : activityCache.keySet()) {
            if (activityCache.get(uriMd5).intValue() ==activity) {
                tempUriMd5List.add(uriMd5);
            }
        }
//        移除
        for (String uriMd5 : tempUriMd5List) {
            activityCache.remove(uriMd5);
            Bitmap bitmap=lruCache.get(uriMd5);
            if (bitmap != null &&!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            lruCache.remove(uriMd5);
            bitmap = null;
        }
        if (!tempUriMd5List.isEmpty()) {
            System.gc();
        }
    }
}
