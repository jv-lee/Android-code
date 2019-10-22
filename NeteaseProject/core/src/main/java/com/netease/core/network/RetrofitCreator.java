package com.netease.core.network;

import com.netease.core.app.ProjectInit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description 此类是 Retrofit创建者
 */
public class RetrofitCreator {

    private final static class RetrofitHolder {
        private final static String BASE_URL = ProjectInit.API_HOST;

        private final static Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    private final static class OKHttpHolder {
        private final static int TIME_OUT = 60;

        private final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 对外提供
     *
     * @return RetrofitService
     */
    public static RetrofitService getRetrofitService() {
        return RetrofitHolder.RETROFIT_CLIENT.create(RetrofitService.class);
    }
}
