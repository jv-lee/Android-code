package com.gionee.gnservice.common.http.okhttp;

import com.gionee.gnservice.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
    private static final String TAG = "LogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        LogUtil.d(TAG, String.format("send post: [%s] %s%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        LogUtil.d(TAG, String.format("recevice response: [%s] %.1fms%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
