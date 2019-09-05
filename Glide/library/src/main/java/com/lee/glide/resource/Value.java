package com.lee.glide.resource;

import android.graphics.Bitmap;
import android.util.Log;

import com.lee.glide.Tool;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description Bitmap的封装
 */
public class Value {

    private static final String TAG = "Value";

    private static Value value;

    public static Value getInstance() {
        if (null == value) {
            synchronized (Value.class) {
                if (null == value) {
                    value = new Value();
                }
            }
        }
        return value;
    }

    /**
     * 查找资源value 的key
     */
    private String key;

    /**
     * 资源
     */
    private Bitmap bitmap;

    /**
     * 使用计数
     */
    private int count;

    private ValueCallback callback;


    /**
     * TODO 使用当前资源设置使用次数+1
     */
    public void useAction() {
        Tool.checkNotEmpty(bitmap);

        if (bitmap.isRecycled()) {
            Log.i(TAG, "useAction: bitmap已经被回收了");
            return;
        }
        count++;
        Log.i(TAG, "useAction: value.count + 1 ->" + count);
    }

    /**
     * TODO 不使用当前资源设置使用次数-1
     */
    public void nonUseAction() {
        count--;
        //不再使用了 回调提示
        if (count <= 0 && callback != null) {
            callback.nonUse(key, value);
        }
        Log.i(TAG, "useAction: value.count - 1 ->" + count);
    }

    /**
     * TODO 释放资源
     */
    public void recycleBitmap() {
        //未使用状态直接释放
        if (count <= 0 && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            value = null;
            System.gc();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ValueCallback getCallback() {
        return callback;
    }

    public void setCallback(ValueCallback callback) {
        this.callback = callback;
    }
}
