package com.lee.code.okhttp.network;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class HttpTask<T> implements Runnable{

    private IHttpRequest mIHttpRequest;

    public HttpTask(String url,T requestData,IHttpRequest httpRequest,CallbackListener listener){
        mIHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(listener);
        String content = new Gson().toJson(requestData);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        mIHttpRequest.execute();
    }
}
