package com.gionee.gnservice.sdk.integral;

import android.os.Build;
import android.text.TextUtils;

import com.gionee.gnservice.base.webview.BaseTokenPostWebViewActivity;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberIntegralLotteryActivity extends BaseTokenPostWebViewActivity {
    private static final String TAG = MemberIntegralLotteryActivity.class.getSimpleName();

    @Override
    protected void webViewLoadUrl(String url, String token) {
        Map<String, String> postParams = new LinkedHashMap<String, String>();
        postParams.put("model", Build.MODEL);
        LogUtil.d(TAG, "model is:" + Build.MODEL);
        if (!TextUtils.isEmpty(token)) {
            postParams.put("token", token);
        }
        postParams.put("imei", PhoneUtil.getIMEI(this));
        postParams.put("nt", NetworkUtil.getNetworkTypeName(this));
        postParams.put("ch", SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        LogUtil.d(TAG, "post params is:" + postParams.toString());
        webViewLoadUrlByPost(url, postParams);
    }

    @Override
    protected String getUrl() {
        return AppConfig.URL.getMemberIntegralLotteryUrl();
    }


    @Override
    protected String getActionbarTitle() {
        return getString(ResourceUtil.getStringId(this, "uc_title_member_integral_lottery_actionbar"));
    }

}
