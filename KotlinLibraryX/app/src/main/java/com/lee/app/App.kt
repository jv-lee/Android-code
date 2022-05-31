package com.lee.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.lee.app.core.NetworkCallbackImpl
import com.lee.library.base.BaseApplication

/**
 *
 * @author jv.lee
 * @date 2020-03-07
 */
class App : BaseApplication() {

    override fun init() {
        Thread(Runnable {
            val networkCallback = NetworkCallbackImpl()
            val builder = NetworkRequest.Builder()
            val request = builder.build()
            val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connMgr.registerNetworkCallback(request, networkCallback)
        }).start()
    }

    override fun unInit() {

    }
}
