package com.lee.okhttprequset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String PATH = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_okhttp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okhttp();
            }
        });

        findViewById(R.id.btn_simple_okhtpp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOkhttp();
            }
        });
    }

    public void okhttp() {
        OkHttpClient okhttp = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(PATH).build();

        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: 请求失败:" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    public void myOkhttp() {
        com.lee.okhttp.core.OkHttpClient client = new com.lee.okhttp.core.OkHttpClient.Builder().build();

        com.lee.okhttp.core.Request request = new com.lee.okhttp.core.Request.Builder().url(PATH).build();
        com.lee.okhttp.core.Call call = client.newCall(request);
        call.enqueue(new com.lee.okhttp.core.Callback() {
            @Override
            public void onFailure(com.lee.okhttp.core.Call call, IOException e) {
                Log.e(TAG, "onFailure: 请求失败:" + e);
            }

            @Override
            public void onResponse(com.lee.okhttp.core.Call call, com.lee.okhttp.core.Response response) throws IOException {
                Log.i(TAG, "onResponse: " + response.string());
            }
        });
    }

}
