package com.lee.okhttprequset;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder().url("https://www.baidu.com").get().build();
        Call call = okHttpClient.newCall(request);

        //同步方法
//        try {
//            Response response = call.execute();
//            String body = response.body().string();
//            response.body().byteStream();
//            response.body().charStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //异步方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败 E:" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("请求成功： code:" + response.code() + " body:" + response.body());
            }
        });
    }
}