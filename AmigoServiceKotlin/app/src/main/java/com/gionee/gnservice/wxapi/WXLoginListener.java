package com.gionee.gnservice.wxapi;

public interface WXLoginListener {
    void onSuccess(String code);
    void onFail(String errMsg);
}
