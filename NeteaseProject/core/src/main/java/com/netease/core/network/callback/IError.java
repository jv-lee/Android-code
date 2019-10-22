package com.netease.core.network.callback;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public interface IError {
    void onError(int code, String message);
}
