package com.gionee.gnservice.common.http.okhttp;

import android.content.Context;

import com.gionee.gnservice.common.http.HttpParam;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHelper {
    private static final String TAG = "OkHttpHelper";
    private OkHttpClient mOkHttpClient;

    public OkHttpHelper(Context context) {
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())//日志拦截器
                .connectTimeout(AppConfig.HttpHelper.CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(AppConfig.HttpHelper.READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(AppConfig.HttpHelper.WRITE_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(context.getCacheDir(), AppConfig.HttpHelper.CACHE_FILE_NAME), AppConfig.HttpHelper.CACHE_MAX_SIZE))
                .build();

    }

    public Response execute(Request request) throws IOException {
        if (mOkHttpClient == null) {
            throw new IllegalArgumentException("okhttpclient can not be null");
        }
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    public void enqueue(Request request, Callback callback) throws IOException {
        if (mOkHttpClient == null) {
            throw new IllegalArgumentException("okhttpclient can not be null");
        }
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


    public Response get(HttpParam httpParam) throws IOException {
        LogUtil.d(TAG, "http get params=" + httpParam.toString());
        OKHttpGetBuilder builder = new OKHttpGetBuilder();
        return execute(builder.build(httpParam));
    }

    public Response post(HttpParam httpParam) throws IOException {
        LogUtil.d(TAG, "http post params=" + httpParam.toString());
        OkHttpPostBuilder builder = new OkHttpPostBuilder();
        return execute(builder.build(httpParam));
    }

}

