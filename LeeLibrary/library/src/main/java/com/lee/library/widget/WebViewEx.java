package com.lee.library.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
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
public class WebViewEx extends WebView implements LifecycleObserver {

    private LifecycleOwner lifecycleOwner;
    private boolean isFailed = false;
    private boolean isPause = false;

    public WebViewEx(Context context) {
        super(context);
        init();
    }

    public WebViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
                final SslErrorHandler mHandler;
                mHandler = handler;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("ssl证书验证失败");
                //不校验https证书
                builder.setPositiveButton("继续", (dialog, which) -> mHandler.proceed());
                //校验证书
                builder.setNegativeButton("取消", (dialog, which) -> mHandler.cancel());
                builder.setOnKeyListener((dialog, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        mHandler.cancel();
                        dialog.dismiss();
                        return true;
                    }
                    return false;
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
                if (webStatusListenerAdapter != null) {
                    webStatusListenerAdapter.callStart();
                    return;
                }
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
                if (webStatusListenerAdapter != null && isFailed) {
                    webStatusListenerAdapter.callSuccess();
                    return;
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
                if (webStatusListenerAdapter != null) {
                    webStatusListenerAdapter.callFailed();
                    return;
                }
                if (webStatusCallBack != null) {
                    webStatusCallBack.callFailed();
                }
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (webStatusListenerAdapter != null) {
                    webStatusListenerAdapter.callProgress(newProgress);
                    return;
                }
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
            if (webStatusListenerAdapter != null) {
                time = System.currentTimeMillis();
                webStatusListenerAdapter.callScroll();
                return;
            }
            if (webStatusCallBack != null) {
                time = System.currentTimeMillis();
                webStatusCallBack.callScroll();
            }
        }
    }

    public void bindLifecycle(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void exResume() {
        if (isPause) {
            onResume();
        }
        isPause = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void exPause() {
        onPause();
        isPause = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void exDestroy() {
        setVisibility(GONE);
        clearCache(true);
        clearHistory();
        removeAllViews();
        destroy();
        isPause = false;
        //取消生命周期监听
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
    }

    private WebStatusListenerAdapter webStatusListenerAdapter;

    public void addWebStatusListenerAdapter(WebStatusListenerAdapter webStatusListenerAdapter) {
        this.webStatusListenerAdapter = webStatusListenerAdapter;
    }

    public abstract static class WebStatusListenerAdapter implements WebStatusCallBack {
        @Override
        public void callStart() {

        }

        @Override
        public void callSuccess() {

        }

        @Override
        public void callFailed() {

        }

        @Override
        public void callProgress(int progress) {

        }

        @Override
        public void callScroll() {

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
