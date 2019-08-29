package com.lee.okhttp.core;

import android.util.Log;

import com.lee.okhttp.chain.ChainManager;
import com.lee.okhttp.chain.Interceptor;
import com.lee.okhttp.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 实现了请求接口的实例 Call的实现类
 */
public class RealCall implements Call {

    private static final String TAG = "OkHttp";

    private boolean executed;
    private boolean isCanceled;

    private OkHttpClient okHttpClient;
    private Request request;

    RealCall(OkHttpClient okHttpClient, Request request) {
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

    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Executed");
            }
            executed = true;
        }


        return null;
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }


    final class AsyncCall implements Runnable {

        Request getRequest() {
            return RealCall.this.request;
        }

        private Callback callback;

        AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            //执行耗时操作
            boolean signalledCallback = false;
            try {
                Response response = getResponseWithInterceptorChain();
                //如果用户取消了请求，回调给用户，失败了
                if (isCanceled) {
                    signalledCallback = true;
                    callback.onFailure(RealCall.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onResponse(RealCall.this, response);
                }

            } catch (IOException e) {
                //责任的划分
                // ture：回调给用户了，是用户操作的时候报错
                if (signalledCallback) {
                    Log.e(TAG, "run: 用户使用过程中出错了...");
                } else {
                    callback.onFailure(RealCall.this, new IOException("OKHttp getResponseWithInterceptorChain e:" + e.toString()));
                }
            } finally {
                //回收处理
                okHttpClient.dispatcher.finished(this);
            }
        }

        private Response getResponseWithInterceptorChain() throws IOException {
            List<Interceptor> interceptors = new ArrayList<>();
            //添加重试拦截器
            interceptors.add(new RetryInterceptor(okHttpClient));

            ChainManager chainManager = new ChainManager(interceptors, 0, request, RealCall.this);
            //最终返回的Response
            return chainManager.proceed(request);
        }


    }
}
