package com.lee.app.core

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description
 */
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        LogUtil.i("网络已连接:")
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        LogUtil.i("网络已断开")
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            LogUtil.d("onCapabilitiesChanged: 网络类型为wifi")
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            LogUtil.d("onCapabilitiesChanged: 蜂窝网络")
        } else {
            LogUtil.d("onCapabilitiesChanged: 其他网络")
        }
    }

}