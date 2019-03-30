package com.lee.code.okhttp.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallBackListener<T> implements CallbackListener {

    private Class<T> responseClass;
    private IJsonDataListener<T> iJsonDataListener;
    private Handler handler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> responseClass,IJsonDataListener<T> listener) {
        this.responseClass = responseClass;
        this.iJsonDataListener = listener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String response = getContent(inputStream);
        final T clazz =new Gson().fromJson(response, responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("lee - >>> error:"+e.toString());
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public void onFailure() {

    }
}
