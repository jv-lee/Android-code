package com.gionee.gnservice.base.webview;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;

import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

public abstract class BaseTokenWebViewActivity extends BaseWebViewActivity {
    private static final String TAG = "BaseTokenWebViewActivity";
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
        loadUrl();
    }

    private void loadUrl() {
        final String url = PreconditionsUtil.checkNotNull(getUrl());
        LoginTask loginTask = new LoginTask(this, mAccountHelper);
        loginTask.execute(url);
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

    private static class LoginTask extends AsyncTask<String, Object, Boolean> {
        private WeakReference<BaseTokenWebViewActivity> weakReference = null;
        private IAccountHelper accountHelper = null;
        private String url = "";

        private LoginTask(BaseTokenWebViewActivity activity, IAccountHelper accountHelper) {
            weakReference = new WeakReference<BaseTokenWebViewActivity>(activity);
            this.accountHelper = accountHelper;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            url = params[0];
            return accountHelper.isLogin();
        }

        @Override
        protected void onPostExecute(Boolean isLogin) {
            BaseTokenWebViewActivity activity = weakReference.get();
            if (null == activity) {
                return;
            }

            if (!isLogin) {
                activity.webViewLoadUrl(url);
                return;
            }

            accountHelper.loadUserInfo(AppConfig.Bbs.getBbsAppID(), new IAccountHelper.OnGetAccountInfoListener() {
                @Override
                public void onSuccess(AccountInfo accountInfo) {
                    BaseTokenWebViewActivity activity = weakReference.get();
                    if (activity == null) {
                        LogUtil.e(TAG, "load user info success, but activity was recycled");
                        return;
                    }

                    if (accountInfo == null) {
                        LogUtil.e(TAG, "load user info success, but accountInfo is null");
                        return;
                    }

                    String token = accountInfo.getToken();
                    if (TextUtils.isEmpty(token)) {
                        LogUtil.e(TAG, "token is null");
                        activity.webViewLoadUrl(url);
                        return;
                    }

                    String encryptToken = activity.encryptToken(token);
                    String newUrl = activity.urlAppendToken(url, encryptToken);
                    activity.webViewLoadUrl(newUrl);
                }

                @Override
                public void onFail(String errMessage) {
                    LogUtil.d(TAG, "get token fail");
                    BaseTokenWebViewActivity activity = weakReference.get();
                    if (null == activity) {
                        return;
                    }
                    activity.webViewLoadUrl(url);
                }
            });
        }
    }

    protected abstract String getUrl();
}
