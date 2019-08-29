package com.lee.okhttp.core;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
 */
public interface Call {
    void enqueue(Callback responseCallback);
}
