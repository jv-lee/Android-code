package com.lee.okhttp.core;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 响应数据 实体封装类
 */
public class Response {

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String string() {
        return body;
    }
}
