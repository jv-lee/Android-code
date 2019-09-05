package com.lee.glide.pool;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.util.TreeMap;

/**
 * @author jv.lee
 * @date 2019-09-04
 * @description
 */
public class BitmapPoolImpl extends LruCache<Integer, Bitmap> implements BitmapPool {

    private static final String TAG = "BitmapPoolImpl";

    /**
     * 为了筛选出 合适的 Bitmap 容器
     */
    private TreeMap<Integer, String> treeMap = new TreeMap<>();

    public BitmapPoolImpl(int maxSize) {
        super(maxSize);
    }

    /**
     * 存入复用池
     *
     * @param bitmap 图片
     */
    @Override
    public void put(Bitmap bitmap) {
        //TODO 条件一 bitmap.isMutable() == true
        if (!bitmap.isMutable()) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            Log.d(TAG, "put: bitmap.isMutable() == true 未满足，无法存入复用池...");
            return;
        }

        //TODO 条件二 bitmapSize < maxSize
        int bitmapSize = getBitmapSize(bitmap);
        if (bitmapSize > maxSize()) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            Log.d(TAG, "put: bitmap.size > pool.maxSize , 未满足，无法存入复用池...");
            return;
        }

        //TODO Bitmap存入LruCache
        put(bitmapSize, bitmap);

        //存入 筛选的容器
        treeMap.put(bitmapSize, null);
        Log.i(TAG, "put: 添加到复用池");
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        /**
         * ALPHA_8 理论上，实际Android自动做处理的，只有透明度8位 1个字节 (w*h*1)
         * RGB_565 理论上，实际Android自动做处理的 R红色 5，G绿色 6，B蓝色 5 16位 2个字节 没有透明度 (w*h*2)
         * ARGB_4444 理论上，实际Android自动做处理的 A透明度 4位，R红色 4位，G绿色 4位，B蓝色 4位 16位 2个字节
         * ARGB_8888（质量最高的） 理论上，实际Android自动做处理的 A 8位 1个字节，R 8位 1个字节 ， G 8位 1个字节， B 8位 1个字节   32位 4个字节
         * 常用的 ARGB_8888 , RGB_565
         */
        int getSize = width * height * (config == Bitmap.Config.ARGB_8888 ? 4 : 2);

        //可以查找到容器里面 和 getSize同样大小，也可以比 getSize还要大的 ， 如果容器中没有put过， 返回null
        Integer key = treeMap.ceilingKey(getSize);

        Log.i(TAG, "get: treeMap.size() ->" + treeMap.size());

        //没有找到合适的 可以复用的 key
        if (key == null) {
            return null;
        }

        //查找容器取出来的key ，必须小于计算出来的 （getSize * 2）
        if (key <= (getSize * 2)) {
            //复用池 如果要取出来，要移除掉，使用其内存后，不给其他对象使用内存
            Log.i(TAG, "get: 从复用池中获得Bitmap");
            return remove(key);
        }
        return null;
    }


    /**
     * 计算bitmap大小
     *
     * @param bitmap
     * @return
     */
    private int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }

    /**
     * 元素大小
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(@NonNull Integer key, @NonNull Bitmap value) {
        return getBitmapSize(value);
    }

    @Override
    protected void entryRemoved(boolean evicted, @NonNull Integer key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        treeMap.remove(key);
    }
}
