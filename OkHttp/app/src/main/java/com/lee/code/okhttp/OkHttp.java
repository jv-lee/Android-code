package com.lee.code.okhttp;

import com.lee.code.okhttp.network.HttpTask;
import com.lee.code.okhttp.network.IHttpRequest;
import com.lee.code.okhttp.network.IJsonDataListener;
import com.lee.code.okhttp.network.JsonCallBackListener;
import com.lee.code.okhttp.network.JsonHttpRequest;
import com.lee.code.okhttp.network.CallbackListener;
import com.lee.code.okhttp.network.ThreadPoolManager;

public class OkHttp {

    public static <T, M> void sendJsonRequest(T requestData, String url, Class<M> response, IJsonDataListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallBackListener<>(response, listener);
        HttpTask httpTask = new HttpTask(url,requestData,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
