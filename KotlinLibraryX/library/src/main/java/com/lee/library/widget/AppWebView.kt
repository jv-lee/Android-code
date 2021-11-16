package com.lee.library.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.LifecycleOwner
import com.lee.library.R
import com.lee.library.dialog.ChoiceDialog
import com.lee.library.dialog.core.CancelListener
import com.lee.library.dialog.core.ConfirmListener
import com.lee.library.lifecycle.ObservableLifecycle

/**
 * @author jv.lee
 */
class AppWebView : WebView, ObservableLifecycle {
    private var lifecycleOwner: LifecycleOwner? = null
    private var isFailed = false
    private var isPause = false
    private var time: Long = 0
    private var firstUrl: String? = null
    private var firstOverrideUrl: String? = null

    private var webStatusListenerAdapter: WebStatusListenerAdapter? = null
    private var webStatusCallBack: WebStatusCallBack? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.savePassword = false
        setWebContentsDebuggingEnabled(true)
        webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                lifecycleOwner ?: return
                val mHandler: SslErrorHandler = handler
                ChoiceDialog(lifecycleOwner as Activity).apply {
                    setTitle(context.getString(R.string.str_ssl_error))
                    setCancelable(true)
                    //不校验https证书
                    confirmListener = ConfirmListener {
                        mHandler.proceed()
                        dismiss()
                    }
                    //校验证书
                    cancelListener = CancelListener {
                        mHandler.cancel()
                        dismiss()
                    }
                }.show()
            }

            /**
             * 高版本重定向
             * @param view
             * @param request
             * @return
             */
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        val scheme = request.url.scheme
                        if (!TextUtils.isEmpty(scheme) && scheme != "http" && scheme != "https") {
                            val intent = Intent(Intent.ACTION_VIEW, request.url)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            view.context.applicationContext.startActivity(intent)
                            return true
                        }
                    } catch (e: Exception) {
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            /**
             * 低版本重定向
             * @param view
             * @param url
             * @return
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //重定向时更新初始URL
                if (view.url == firstUrl || view.url == firstOverrideUrl) {
                    firstOverrideUrl = url
                }
                return if (url.startsWith("https://") || url.startsWith("https://")) {
                    view.loadUrl(url)
                    false
                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view.context.applicationContext.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    true
                }
            }

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            //开始加载网页
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                getSettings().blockNetworkImage = true
                isFailed = true
                webStatusListenerAdapter?.callStart()
                webStatusCallBack?.callStart()
            }

            //加载网页成功
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                getSettings().blockNetworkImage = false
                if (!getSettings().loadsImagesAutomatically) {
                    //设置wenView加载图片资源
                    getSettings().blockNetworkImage = false
                    getSettings().loadsImagesAutomatically = true
                }
                webStatusListenerAdapter?.callSuccess()
                webStatusCallBack?.callSuccess()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                isFailed = false
                webStatusListenerAdapter?.callFailed()
                webStatusCallBack?.callFailed()
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                webStatusListenerAdapter?.callProgress(newProgress)
                webStatusCallBack?.callProgress(newProgress)
            }
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (System.currentTimeMillis() - time >= 1000) {
            webStatusListenerAdapter?.apply {
                time = System.currentTimeMillis()
                callScroll()
            }
            webStatusCallBack?.apply {
                time = System.currentTimeMillis()
                callScroll()
            }
        }
    }

    override fun canGoBack(): Boolean {
        return if (url == firstUrl || url == firstOverrideUrl) {
            false
        } else {
            super.canGoBack()
        }
    }

    /**
     * 初始化首个加载地址 标记地址 复用webView时做back操作
     *
     * @param url
     */
    fun initUrl(url: String) {
        firstUrl = url
        firstOverrideUrl = url
        loadUrl(url)
    }

    fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onLifecycleResume() {
        super.onLifecycleResume()
        if (isPause) {
            onResume()
        }
        isPause = false
    }

    override fun onLifecyclePause() {
        super.onLifecyclePause()
        onPause()
        isPause = true
    }

    override fun onLifecycleDestroy() {
        super.onLifecycleDestroy()
        visibility = View.GONE
        clearCache(true)
        clearHistory()
        removeAllViews()
        destroy()
        isPause = false
        //取消生命周期监听
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }

    fun destroyView() {
        loadEmpty()
        clearHistory()
        (parent as ViewGroup).removeAllViews()
    }

    fun loadEmpty() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
    }

    interface WebStatusCallBack {
        fun callStart()
        fun callSuccess()
        fun callFailed()
        fun callProgress(progress: Int)
        fun callScroll()
    }

    abstract class WebStatusListenerAdapter : WebStatusCallBack {
        override fun callStart() {}
        override fun callSuccess() {}
        override fun callFailed() {}
        override fun callProgress(progress: Int) {}
        override fun callScroll() {}
    }

    fun setWebStatusCallBack(webStatusCallBack: WebStatusCallBack?) {
        this.webStatusCallBack = webStatusCallBack
    }

    fun addWebStatusListenerAdapter(webStatusListenerAdapter: WebStatusListenerAdapter?) {
        this.webStatusListenerAdapter = webStatusListenerAdapter
    }

}