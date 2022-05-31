package com.lee.app.core

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.lee.library.utils.LogUtil

/**
 *
 * @author jv.lee
 * @date 2020-03-07
 *
 */
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    var firstCode = 0
    var lastCode = 0

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        firstCode = network.toString().toInt()
        LogUtil.i("${network}网络：连接成功")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        lastCode = network.toString().toInt()
        if (lastCode >= firstCode) {
            LogUtil.i("${network}网络：断开连接")
        }
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                LogUtil.d("onCapabilitiesChanged: 网络类型为wifi :$network")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                LogUtil.d("onCapabilitiesChanged: 蜂窝网络:$network")
            } else {
                LogUtil.d("onCapabilitiesChanged: 其他网络:$network")
            }
        }
    }

}