package com.lee.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author jv.lee
 */
public class WebViewEx extends WebView {

    private boolean isFailed = false;

    public WebViewEx(Context context) {
        this(context, null);
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        setWebContentsDebuggingEnabled(true);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //不校验https证书
                handler.proceed();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String scheme = request.getUrl().getScheme();
                if (!TextUtils.isEmpty(scheme) && !scheme.equals("http") && !scheme.equals("https")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().getApplicationContext().startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                getSettings().setBlockNetworkImage(true);
                isFailed = true;
                if (webStatusCallBack != null) {
                    webStatusCallBack.callStart();
                }
            }

            //加载网页成功
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getSettings().setBlockNetworkImage(false);
                if (!getSettings().getLoadsImagesAutomatically()) {
                    //设置wenView加载图片资源
                    getSettings().setBlockNetworkImage(false);
                    getSettings().setLoadsImagesAutomatically(true);
                }
                if (webStatusCallBack != null && isFailed) {
                    webStatusCallBack.callSuccess();
                }
            }

            //加载网页错误
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isFailed = false;
                if (webStatusCallBack != null) {
                    webStatusCallBack.callFailed();
                }
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (webStatusCallBack != null) {
                    webStatusCallBack.callProgress(newProgress);
                }
            }
        });

    }

    long time;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (System.currentTimeMillis() - time >= 1000) {
            if (webStatusCallBack != null) {
                time = System.currentTimeMillis();
                webStatusCallBack.callScroll();
            }
        }
    }

    public interface WebStatusCallBack {
        void callStart();

        void callSuccess();

        void callFailed();

        void callProgress(int progress);

        void callScroll();
    }

    private WebStatusCallBack webStatusCallBack;

    public void setWebStatusCallBack(WebStatusCallBack webStatusCallBack) {
        this.webStatusCallBack = webStatusCallBack;
    }
}
