package com.lee.library.tools

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.lee.library.widget.AppWebView

/**
 * @author jv.lee
 * @date 2020/4/8
 * @description 全局单例webView
 */
class WebViewTools constructor(context: Context) {

    private var web: AppWebView? = null

    init {
        AppWebView(context).also {
            web = it
            web?.run {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                loadEmpty()
            }
        }
    }

    companion object {
        @Volatile
        private var instance: WebViewTools? = null

        fun get(context: Context) = instance ?: synchronized(WebViewTools::class.java) {
            instance ?: WebViewTools(context).also { instance = it }
        }

        fun getWeb(context: Context) = get(context).web
    }

    fun onDestroy() {
        web = null
        instance = null
    }

}