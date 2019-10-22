package com.netease.core.network.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description 对回掉处理类对象
 */
public class RequestCallback implements Callback {

    private final IError iError;
    private final IFailure iFailure;
    private final IRequest iRequest;
    private final ISuccess iSuccess;

    public RequestCallback(IError iError, IFailure iFailure, IRequest iRequest, ISuccess iSuccess) {
        this.iError = iError;
        this.iFailure = iFailure;
        this.iRequest = iRequest;
        this.iSuccess = iSuccess;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful() && call.isExecuted() && iSuccess != null) {
            iSuccess.onSuccess(response.message());
        } else if (iError != null) {
            iError.onError(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (iFailure != null) {
            iFailure.onFailure(t);
        }
        if (iRequest != null) {
            iRequest.onRequestEnd();
        }
    }
}
