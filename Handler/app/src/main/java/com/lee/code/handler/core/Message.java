package com.lee.code.handler.core;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class Message {
    /**
     * handler对象
     */
    public Handler target;
    /**
     * 消息内容
     */
    public Object obj;
    /**
     * 标识
     */
    public int what;

    public Message() {
    }

    public Message(Object obj) {
        this.obj = obj;
    }

    @NonNull
    @Override
    public String toString() {
        return obj.toString();
    }
}
