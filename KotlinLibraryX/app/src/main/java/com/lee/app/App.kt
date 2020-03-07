package com.lee.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.lee.app.core.NetworkCallbackImpl


/**
 * @author jv.lee
 * @date 2020-03-07
 * @description
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Thread(Runnable {
            val networkCallback = NetworkCallbackImpl()
            val builder = NetworkRequest.Builder()
            val request = builder.build()
            val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connMgr.registerNetworkCallback(request, networkCallback)
        }).start()

    }
}
