package com.lee.okhttp.core;

import com.lee.okhttp.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 请求参数 数据封装类
 */
public final class Request {

    public static final String GET = "GET";
    public static final String POST = "POST";

    /**
     * 请求地址
     */
    private String url;

    /**
     * 默认情况下使用GET
     */
    private String requestMethod;

    /**
     * 请求头map
     */
    private Map<String, String> headerList;

    /**
     * 请求体
     */
    private RequestBody requestBody;

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public Map<String, String> getHeaderList() {
        return headerList;
    }

    public Request() {
        this(new Builder());
    }

    public Request(Builder builder) {
        this.url = builder.url;
        this.requestMethod = builder.requestMethod;
        this.headerList = builder.mHeaderList;
        this.requestBody = builder.requestBody;
    }

    public final static class Builder {

        private String url;

        private String requestMethod = GET;

        private Map<String, String> mHeaderList = new HashMap<>();

        private RequestBody requestBody;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.requestMethod = GET;
            return this;
        }

        public Builder post(RequestBody requestBody) {
            this.requestMethod = POST;
            this.requestBody = requestBody;
            return this;
        }

        public Builder addRequestHeader(String key, String value) {
            mHeaderList.put(key, value);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

}
