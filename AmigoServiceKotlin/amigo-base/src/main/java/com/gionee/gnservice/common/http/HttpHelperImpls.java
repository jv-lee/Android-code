package com.gionee.gnservice.common.http;

import android.content.Context;

import com.gionee.gnservice.common.http.okhttp.OkHttpHelper;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.io.IOException;

import okhttp3.Response;

public class HttpHelperImpls implements IHttpHelper {
    private static final String TAG = "HttpHelperImpl";
    private OkHttpHelper mOkHttpHelper;

    public HttpHelperImpls(Context context) {
        mOkHttpHelper = new OkHttpHelper(context);
    }

    @Override
    public IHttpResponse get(HttpParam params) throws IOException {
        PreconditionsUtil.checkNotNull(params);
        final Response response = mOkHttpHelper.get(params);
        if (response == null || !response.isSuccessful()) {
            LogUtil.d(TAG, "get response fail");
            return null;
        }
        return new IHttpResponse() {

            @Override
            public byte[] getBytes() throws IOException {
                return response.body().bytes();
            }

            @Override
            public String getString() {
                try {
                    String result = response.body().string();
                    LogUtil.d(TAG, "get response=" + result);
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

    }

    @Override
    public IHttpResponse post(HttpParam params) throws IOException {
        PreconditionsUtil.checkNotNull(params);
        final Response response = mOkHttpHelper.post(params);
        if (response == null || !response.isSuccessful()) {
            LogUtil.d(TAG, "get response fail");
            return null;
        }
        return new IHttpResponse() {

            @Override
            public byte[] getBytes() throws IOException {
                return response.body().bytes();
            }

            @Override
            public String getString() {
                try {
                    String result = response.body().string();
                    LogUtil.d(TAG, "get response=" + result);
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}
