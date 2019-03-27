package com.lee.code.okhttp.network;

public interface IHttpRequest {

    //封装请求接口
    void setUrl(String ulr);

    void setData(byte[] data);

    void setListener(CallbackListener callbackListener);

    void execute();
}
