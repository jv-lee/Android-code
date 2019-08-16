package com.gionee.gnservice.base.webview;

import android.text.TextUtils;
import android.util.Base64;

import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class BaseTokenPostWebViewActivity extends BaseWebViewActivity {
    private static final String TAG = BaseTokenPostWebViewActivity.class.getSimpleName();
    private IAccountHelper mAccountHelper;

    @Override
    protected void initVariables() {
        super.initVariables();
        mAccountHelper = PreconditionsUtil.checkNotNull(appContext().accountHelper());
    }

    @Override
    protected void reLoadUrl() {
        loadUrl();
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        if (NetworkUtil.isConnected(this)) {
            loadUrl();
        } else {
            showNetworkErrorView();
        }
    }

    private void loadUrl() {
        final long startGetTokenTime = System.currentTimeMillis();
        final String url = PreconditionsUtil.checkNotNull(getUrl());
        mAccountHelper.loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                String token = accountInfo.getToken();
                LogUtil.d(TAG, "token is:" + token + ";use time is:" + (System.currentTimeMillis() - startGetTokenTime));
                if (TextUtils.isEmpty(token)) {
                    LogUtil.e(TAG, "token is null");
                    showNetworkErrorView();
                    return;
                }
                webViewLoadUrl(url, encryptToken(token));
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.d(TAG, "get token fail");
                showNetworkErrorView();
            }
        });

    }

    private String urlAppendToken(String url, String token) {
        boolean hasParams = !TextUtils.isEmpty(url) && url.contains("?") && url.contains("=");
        StringBuffer sb = new StringBuffer(url);
        if (!TextUtils.isEmpty(token)) {
            if (hasParams) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("token=");
            sb.append(token);
        }
        return sb.toString();
    }

    protected String encryptToken(String token) {
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        try {
            return Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void webViewLoadUrlByPost(String url, Map<String, String> postParams) {
        PreconditionsUtil.checkNotNull(url);
        PreconditionsUtil.checkNotNull(postParams);
        if (mWebView == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : postParams.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String post = sb.toString();
        if (!TextUtils.isEmpty(post) && post.endsWith("&")) {
            post = post.substring(0, post.length() - 1);

        }

        url = !TextUtils.isEmpty(post) ? url + "?" + post : url;
        LogUtil.d(TAG, "url is：" + url);
        mWebView.loadUrl(url);
    }

    protected abstract void webViewLoadUrl(String url, String token);

    protected abstract String getUrl();
}
