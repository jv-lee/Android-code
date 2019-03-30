package com.lee.code.okhttp.network;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
