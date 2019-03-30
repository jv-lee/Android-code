package com.lee.code.okhttp.network;

public interface IJsonDataListener<T> {

    void onSuccess(T data);

    void onFailure();

}
