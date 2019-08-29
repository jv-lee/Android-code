package com.lee.okhttp.core;


/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description OkHttpClient 使用实例
 */
public class OkHttpClient {

    Dispatcher dispatcher;

    int retryCount;

    public OkHttpClient() {
        this(new Builder());
    }

    private OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.retryCount = builder.retryCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public final static class Builder {

        /**
         * 事件分发器
         */
        Dispatcher dispatcher = new Dispatcher();

        /**
         * 重试次数
         */
        int retryCount = 3;

        public Builder() {
        }

        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder setRetryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public OkHttpClient build() {
            return new OkHttpClient(this);
        }
    }

    /**
     * 通过该方法 把 OkHttpClient 实例 传递给了 Call的实现类  RealCall（okHttpClient,request）
     *
     * @param request 请求参数
     * @return RealCall -> Call
     */
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    Dispatcher dispatcher() {
        return dispatcher;
    }

}
