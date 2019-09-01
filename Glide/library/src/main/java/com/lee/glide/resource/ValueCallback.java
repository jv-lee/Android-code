package com.lee.glide.resource;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 给Value（Bitmap不再使用的回调接口）
 */
public interface ValueCallback {

    /**
     *  监听Value（bitmap） 不再使用
     * @param key
     * @param value
     */
    void nonUse(String key, Value value);
}
