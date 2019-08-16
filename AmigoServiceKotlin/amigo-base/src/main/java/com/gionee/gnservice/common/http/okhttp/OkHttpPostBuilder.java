package com.gionee.gnservice.common.http.okhttp;

import android.text.TextUtils;

import com.gionee.gnservice.common.http.HttpParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpPostBuilder extends OkHttpRequestBuilder {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected String params2Json(Map<String, String> params) {
        try {
            JSONObject body = new JSONObject();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    body.put(entry.getKey(), entry.getValue());
                }
            }
            return body.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Request build(HttpParam params) {
        if (params == null) {
            throw new IllegalArgumentException("http params can not be null");
        }
        //url
        String url = params.getUrl();
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("rl can not be null");
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        //params

        //params需要转化成参数
        if (params.isPostParamJson()) {
            String content = params2Json(params.getParams());
            RequestBody requestBody = null;
            if (!TextUtils.isEmpty(content)) {
                requestBody = RequestBody.create(JSON, content);
            }
            if (requestBody != null) {
                builder.post(requestBody);
            }
        } else {
            Map<String, String> paramMaps = params.getParams();
            FormBody.Builder encodingBuilder = new FormBody.Builder();
            if (paramMaps != null && paramMaps.size() > 0) {
                for (Map.Entry<String, String> param : paramMaps.entrySet()) {
                    encodingBuilder.add(param.getKey(), param.getValue());
                }
                //appendParams(encodingBuilder, paramMaps);
                builder.post(encodingBuilder.build());
            }
        }

        //tag
        Object tag = params.getTag();
        if (tag != null) {
            builder.tag(tag);
        }

        //heads
        Map<String, String> headers = params.getHeaders();
        if (headers != null && headers.size() > 0) {
            appendHeaders(builder, headers);
        }

        return builder.build();
    }
}
