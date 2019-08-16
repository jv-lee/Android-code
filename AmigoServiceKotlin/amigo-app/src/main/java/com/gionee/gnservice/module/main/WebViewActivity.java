package com.gionee.gnservice.module.main;

import android.content.Intent;
import android.text.TextUtils;
import com.gionee.gnservice.base.webview.BaseWebViewActivity;
import com.gionee.gnservice.utils.ResourceUtil;

/**
 * Created by caocong on 7/13/17.
 */
public class WebViewActivity extends BaseWebViewActivity {
    private String mUrl;

    @Override
    protected void initVariables() {
        super.initVariables();
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
    }

    @Override
    protected String getActionbarTitle() {
        return ResourceUtil.getString(this, "uc_privilege_action_title");
    }


    @Override
    protected void postInitView() {
        super.postInitView();
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    protected void reLoadUrl() {
        super.reLoadUrl();
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }
}
