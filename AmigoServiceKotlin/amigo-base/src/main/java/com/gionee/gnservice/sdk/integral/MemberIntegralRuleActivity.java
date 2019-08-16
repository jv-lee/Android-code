package com.gionee.gnservice.sdk.integral;

import com.gionee.gnservice.base.webview.BaseWebViewActivity;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.utils.ResourceUtil;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberIntegralRuleActivity extends BaseWebViewActivity {
    private static final String TAG = MemberIntegralRuleActivity.class.getSimpleName();

    @Override
    protected String getActionbarTitle() {
        return ResourceUtil.getString(this, "uc_txt_member_integral_rule_actionbar");
    }

    @Override
    protected void postInitView() {
        super.postInitView();
        mWebView.loadUrl(AppConfig.URL.getMemberIntegralRuleUrl());
    }

    @Override
    protected void reLoadUrl() {
        super.reLoadUrl();
        mWebView.loadUrl(AppConfig.URL.getMemberIntegralRuleUrl());
    }

}
