package com.gionee.gnservice.common.http.okhttp;

import com.gionee.gnservice.common.http.HttpParam;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

public abstract class OkHttpRequestBuilder {

    public abstract Request build(HttpParam params);

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }
        builder.headers(headerBuilder.build());
    }

}
