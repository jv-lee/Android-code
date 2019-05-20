package com.lee.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author jv.lee
 */
public class WebViewEx extends WebView {

    private boolean isFailed = false;

    public WebViewEx(Context context) {
        this(context,null);
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        this(context, attrs,0);
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
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return true;
            }
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isFailed = true;
                if (webStatusCallBack != null) {
                    webStatusCallBack.callStart();
                }
            }
            //加载网页成功
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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
        setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (webStatusCallBack != null) {
                    webStatusCallBack.callProgress(newProgress);
                }
            }
        });

    }

    public interface WebStatusCallBack {
        void callStart();
        void callSuccess();
        void callFailed();
        void callProgress(int progress);
    }

    private WebStatusCallBack webStatusCallBack;

    public void setWebStatusCallBack(WebStatusCallBack webStatusCallBack) {
        this.webStatusCallBack = webStatusCallBack;
    }
}
