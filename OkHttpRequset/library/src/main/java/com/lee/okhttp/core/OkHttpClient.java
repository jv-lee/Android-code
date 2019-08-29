package com.lee.okhttp.core;


/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
 */
public class OkHttpClient {

    Dispatcher dispatcher;

    public OkHttpClient() {
        this(new Builder());
    }

    public OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
    }

    public final static class Builder {
        Dispatcher dispatcher = new Dispatcher();

        public Builder() {
        }

        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
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

    public Dispatcher dispatcher() {
        return dispatcher;
    }

}
