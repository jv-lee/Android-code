package com.gionee.gnservice.common.http.okhttp;

import android.net.Uri;
import android.text.TextUtils;

import com.gionee.gnservice.common.http.HttpParam;

import java.util.Map;

import okhttp3.Request;

public class OKHttpGetBuilder extends OkHttpRequestBuilder {

    protected String appendParams(String url, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().toString();
    }

    @Override
    public Request build(HttpParam params) {
        if (params == null) {
            throw new IllegalArgumentException("http params can not be null");
        }
        String url = params.getUrl();
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("rl can not be null");
        }

        Request.Builder builder = new Request.Builder();
        Map<String, String> paramMaps = params.getParams();
        if (paramMaps != null && paramMaps.size() > 0) {
            url = appendParams(url, paramMaps);
        }
        builder.url(url);

        Object tag = params.getTag();
        if (tag != null) {
            builder.tag(tag);
        }

        Map<String, String> headers = params.getHeaders();
        if (headers != null && headers.size() > 0) {
            appendHeaders(builder, headers);
        }
        return builder.get().build();
    }
}
