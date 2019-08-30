package com.lee.okhttp.interceptor;

import android.util.Log;

import com.lee.okhttp.OkHttpClient;
import com.lee.okhttp.core.InterceptorManager;
import com.lee.okhttp.core.Response;

import java.io.IOException;

/**
 * @author jv.lee
 * @date 2019-08-29
 * @description 重试拦截器
 */
public class RetryInterceptor implements Interceptor {

    private static final String TAG = "OkHttp";

    private OkHttpClient client;

    public RetryInterceptor(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        InterceptorManager chainManager = (InterceptorManager) chain;
        IOException ioException = null;

        if (client.getRetryCount() != 0) {
            for (int i = 0; i < client.getRetryCount(); i++) {
                try {
                    Log.i(TAG, "intercept: 重试拦截器 返回response");

                    //如果没有异常循环就结束了
                    return chain.proceed(chainManager.request());
                } catch (IOException e) {
                    ioException = e;
                }
            }
        }
        assert ioException != null;
        throw ioException;
    }
}
