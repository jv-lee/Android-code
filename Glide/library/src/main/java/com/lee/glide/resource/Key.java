package com.lee.glide.resource;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 唯一的描述
 */
public class Key {

    private String key;

    public Key(String key) {
        //加密处理 Tool加密方法
        this.key = String.valueOf(key.hashCode());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
