package com.lee.code.okhttp.network;

import java.io.InputStream;

interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
