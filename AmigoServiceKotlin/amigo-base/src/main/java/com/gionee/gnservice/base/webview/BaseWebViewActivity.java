package com.gionee.gnservice.base.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;

import java.io.UnsupportedEncodingException;

import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by caocong on 2/6/17.
 */
public abstract class BaseWebViewActivity extends BaseSdkActivity {
    private static final String TAG = BaseWebViewActivity.class.getSimpleName();
    private static final String DEFAULT_COLOR = "232,76,61,1";
    protected WebView mWebView;
    protected ProgressBar mProgressView;

    private ViewGroup mLayoutWebView;
    private ViewGroup mLayoutLoadFail;

    private UploadHandler mUploadHandler;
    //private boolean mNeedClearHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNeedShowNetworkError = false;
        //registerNetStateReceiver();
    }

    @Override
    protected void initView() {
        mWebView = getView(getWidgetId(this, "webview"));
        mProgressView = getView(getWidgetId(this, "progress_webview_loading"));
        mProgressView.setMax(100);
        mLayoutWebView = getView(getWidgetId(this, "layout_webview"));
        mLayoutLoadFail = getView(getWidgetId(this, "layout_webview_load_fail"));
        mLayoutLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RepeatClickUtil.canRepeatClick(v)) {
                    return;
                }
                reLoadUrl();
            }
        });
    }


    @Override
    protected int getLayoutResId() {
        return getLayoutId(this, "uc_activity_webview");
    }

    /*protected void registerNetStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetStateReceiver, filter);
    }

    private BroadcastReceiver mNetStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d(TAG, "NetStateReceiver recevier action is:" + action);
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action) && NetworkUtil.isConnected(
                    BaseWebViewActivity.this)) {
                mNeedClearHistory = true;
                reLoadUrl();
            }
        }
    };*/

    protected void reLoadUrl() {
        LogUtil.d(TAG, "reLoadUrl");
    }

    protected void showLoadingView() {
        LogUtil.d(TAG, "showLoading");
        mLayoutWebView.setVisibility(View.VISIBLE);
        mLayoutLoadFail.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    protected void showNetworkErrorView() {
        LogUtil.d(TAG, "showNetworkErrorView");
        mLayoutWebView.setVisibility(View.GONE);
        mLayoutLoadFail.setVisibility(View.VISIBLE);
    }

    protected void showLoadSuccessView() {
        LogUtil.d(TAG, "showLoadSuccessView");
        mLayoutWebView.setVisibility(View.VISIBLE);
        mLayoutLoadFail.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mNetStateReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initVariables() {
        mUploadHandler = new UploadHandler(this);
    }

    @Override
    protected void postInitView() {
        configWebView();
    }

    private String urlAppendColor(String url) {
        return appendColor(url);
    }


    protected void webViewLoadUrl(String url) {
        LogUtil.d(TAG, "webViewLoadUrl,load url is:" + url);
        if (mWebView != null && !TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    private String appendColor(String url) {
        boolean hasParams = !TextUtils.isEmpty(url) && url.contains("?") && url.contains("=");
        /*String color = ChameleonColorManager.isPowerSavingMode() ? intToHex(
                ChameleonColorManager.getAppbarColor_A1()) : DEFAULT_COLOR;*/
        String color = DEFAULT_COLOR;
        StringBuffer sb = new StringBuffer(url);
        if (hasParams) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append("themecolor=");
        sb.append(color);
        return sb.toString();
    }

    private String intToHex(int originalColor) {
        float opaque = (((originalColor & 0xff000000) >> 24) & 0x0000ff) / 255.0f;
        int originalRedColor = (originalColor & 0xff0000) >> 16;
        int originalGreenColor = (originalColor & 0x00ff00) >> 8;
        int originalBlueColor = (originalColor & 0x0000ff);
        return originalRedColor + "," + originalGreenColor + "," + originalBlueColor + "," + opaque;
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, "shouldOverrideUrlLoading: " + url);
            if (!url.startsWith("http") && !url.startsWith("https")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.d(TAG, "onPageFinished URL: " + url);
            super.onPageFinished(view, url);
            if (!NetworkUtil.isConnected(BaseWebViewActivity.this)) {
                //ToastUtil.showLong(BaseWebViewActivity.this, R.string.uc_network_exception);
                showNetworkErrorView();
                return;
            }
            showLoadSuccessView();
            mWebView.getSettings().setBlockNetworkImage(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.d(TAG, "onPageStarted URL: " + url);
            super.onPageStarted(view, url, favicon);
            showLoadingView();
            mWebView.getSettings().setBlockNetworkImage(true);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            /*LogUtil.d(TAG, "doUpdateVisitedHistory " + url);
            if (mNeedClearHistory) {
                if (mWebView != null) {
                    mWebView.clearHistory();
                }
                mNeedClearHistory = false;
            }*/
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            return mUploadHandler.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressView.setProgress(newProgress);
        }
    };

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                LogUtil.d(TAG, "OnKeyListener goBack");
                mWebView.goBack();
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int key, KeyEvent event) {
        if (key == KeyEvent.KEYCODE_BACK) {
            LogUtil.d(TAG, "onKeyDown KEYCODE_BACK");
        }
        return super.onKeyDown(key, event);
    }

    private DownloadListener mDownloadListener = new DownloadListener() {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    private void configWebView() {
        if (mWebView == null) {
            return;
        }
        mWebView.addJavascriptInterface(new JsInterfaceImpl(mWebView, this), JsInterfaceImpl.JS_INTERFACE_NAME);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setOnKeyListener(mOnKeyListener);
        mWebView.setDownloadListener(mDownloadListener);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setBlockNetworkImage(true);

        //local storage
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
//        try {
//            Class.forName("com.amigo.utils.ProductConfiguration");
//            webSettings.setUserAgentString(ProductConfiguration.getUAString());
//        } catch (ClassNotFoundException e) {
//            LogUtil.e(e.getMessage());
//        }
    }

    protected String encryptToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            return null;
        }
        try {
            return Base64.encodeToString(token.getBytes("utf-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mUploadHandler.onResultForAndroidL(resultCode, intent);
    }

    //清除webview的cookie
    public static void clearCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
    }

}
