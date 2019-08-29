package com.lee.okhttp.core;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
 */
public class RealCall implements Call {

    private boolean executed;

    private OkHttpClient okHttpClient;
    private Request request;

    public RealCall(OkHttpClient okHttpClient, Request request) {
        this.okHttpClient = okHttpClient;
        this.request = request;
    }

    @Override
    public void enqueue(Callback responseCallback) {

        //不能被重复执行
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Executed");
            }
            executed = true;
        }

        okHttpClient.dispatcher().enqueue(new AsyncCall(responseCallback));
    }


    final static class AsyncCall implements Runnable {

        private Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {

        }
    }
}
