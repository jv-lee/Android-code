package com.lee.code.glide.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.lee.code.glide.cache.disk.DiskLruCache;
import com.lee.code.glide.cache.disk.IOUtil;
import com.lee.code.glide.request.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskBitmapCache implements BitmapCache {

    private DiskLruCache diskLruCache;

    private static volatile DiskBitmapCache instance;

    private String imageCachePath = "Image";

    private static final byte[]lock = new byte[0];

    private  int MB = 1024*1024;

    private  int maxDiskSize = 50 * MB;

    private DiskBitmapCache(Context context){
        File cacheFile = getImageCacheFile(context,imageCachePath);
        if (!cacheFile.exists()){
            cacheFile.mkdirs();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheFile,getAppVersion(context),1,maxDiskSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskBitmapCache getInstance(Context context){
        if (instance == null){
            synchronized (lock){
                if (instance == null){
                    instance = new DiskBitmapCache(context);
                }
            }
        }
        return  instance;
    }

    private int getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    private File getImageCacheFile(Context context, String imageCachePath) {
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = context.getExternalCacheDir().getPath();
        }else {
            path = context.getCacheDir().getPath();
        }
        return new File(path + File.separator +imageCachePath);
    }


    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        DiskLruCache.Editor editor;
        OutputStream outputStream = null;
        try {
            editor = diskLruCache.edit(request.getUriMD5());
            outputStream = editor.newOutputStream(0);
            if (presetBitmap2Disk(outputStream,bitmap)){
                editor.commit();
            }else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(outputStream);
        }
    }

    private boolean presetBitmap2Disk(OutputStream outputStream, Bitmap bitmap) {
        BufferedOutputStream bufferedOutputStream = null;
        try {

            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
            bufferedOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(bufferedOutputStream);
        }
        return false;
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        InputStream stream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(request.getUriMD5());
            if (snapshot != null){
                stream = snapshot.getInputStream(0);

                return BitmapFactory.decodeStream(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(stream);
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            diskLruCache.remove(request.getUriMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int activityCode) {

    }
}
