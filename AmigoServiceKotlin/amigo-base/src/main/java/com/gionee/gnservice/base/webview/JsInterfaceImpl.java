package com.gionee.gnservice.base.webview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class JsInterfaceImpl implements IJavascriptInterface {
    private static final String TAG = JsInterfaceImpl.class.getSimpleName();
    private WebView mWebView;
    private WeakReference<Activity> mRefActivity;

    public JsInterfaceImpl(WebView webView, Activity activity) {
        PreconditionsUtil.checkNotNull(webView);
        PreconditionsUtil.checkNotNull(activity);
        this.mWebView = webView;
        this.mRefActivity = new WeakReference<Activity>(activity);
    }

    @JavascriptInterface
    @Override
    public void setActionBar(final String title, final boolean displayHomeAsUpEnabled) {
        LogUtil.d(TAG, "setActionBar");
        if (isEnglishLanguage()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Activity activity = mRefActivity.get();
                if (activity == null) {
                    return;
                }
                ActionBar actionBar = activity.getActionBar();
                if (actionBar == null) {
                    return;
                }
                LogUtil.i(TAG, "call method setActionBar,title is:" + title + ",displayHomeAsUpEnabled is:" + displayHomeAsUpEnabled);
                actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(title);
            }
        };
        runInMainThread(runnable);

    }

    @JavascriptInterface
    @Override
    public void startAndroidActivity(final String json) {
        LogUtil.d(TAG, "startAndroidActivity");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jos = new JSONObject(json);
                    String action = jos.getString("action");
                    String packageName = jos.getString("package");
                    LogUtil.i(TAG, "packageName: " + packageName + ", action: " + action );
                    if (!TextUtils.isEmpty(action) && !TextUtils.isEmpty(packageName)) {
                        Intent intent = new Intent();
                        intent.setPackage(packageName);
                        intent.setAction(action);
                        Activity activity = mRefActivity.get();
                        if (activity != null) {
                            LogUtil.i(TAG, "really start activity");
                            activity.startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        runInMainThread(runnable);
    }

    @JavascriptInterface
    @Override
    public boolean isAndroidActivityExist(final String json) {
        LogUtil.d(TAG, "isAndroidActivityExist");
        try {
            JSONObject jos = new JSONObject(json);
            String action = jos.getString("action");
            String packageName = jos.getString("package");
            Intent intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(action);
            LogUtil.i(TAG, "resolve activity action: " + action);
            return intent.resolveActivity(mWebView.getContext().getPackageManager()) != null;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void runInMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    private boolean isEnglishLanguage() {
        String locale = Locale.getDefault().getLanguage();
        return locale.equals(Locale.ENGLISH.getLanguage());
    }

}
