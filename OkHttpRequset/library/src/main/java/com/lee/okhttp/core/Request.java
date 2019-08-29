package com.lee.okhttp.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
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
    private String requestMethod = GET;

    /**
     * 请求头map
     */
    private Map<String, String> mHeaderList = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public Map<String, String> getmHeaderList() {
        return mHeaderList;
    }

    public Request() {
        this(new Builder());
    }

    public Request(Builder builder) {
        this.url = builder.url;
        this.requestMethod = builder.requestMethod;
        this.mHeaderList = builder.mHeaderList;
    }

    public final static class Builder {

        /**
         * 请求地址
         */
        private String url;

        /**
         * 默认情况下使用GET
         */
        private String requestMethod = GET;

        /**
         * 请求头map
         */
        private Map<String, String> mHeaderList = new HashMap<>();

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.requestMethod = GET;
            return this;
        }

        public Builder post() {
            this.requestMethod = POST;
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
