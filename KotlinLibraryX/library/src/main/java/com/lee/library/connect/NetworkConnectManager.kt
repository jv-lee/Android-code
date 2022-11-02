package com.lee.library.connect

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

/**
 * 全局网络连接监听管理器
 * required by ConnectivityManager.registerNetworkCallback: android.permission.ACCESS_NETWORK_STATE
 * @author jv.lee
 * @date 2020-03-07
 */
class NetworkConnectManager : ConnectivityManager.NetworkCallback() {

    companion object {
        private const val TAG = "NetworkConnectManager"
        val instance by lazy { NetworkConnectManager() }
    }

    /** 当前网络连接的id */
    private var connectID = 0

    /** 当前网络是否连接 */
    private var isConnect = false

    /** 当前网络连接类型 */
    private var networkType: NetworkType = NetworkType.Other

    /** 网络监听回调接口集合 */
    private val callbacks = ArrayList<NetworkConnectCallback>()

    fun init(app: Application) {
        val builder = NetworkRequest.Builder()
        val request = builder.build()
        val connMgr = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connMgr.registerNetworkCallback(request, this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        connectID = network.toString().toInt()

        Log.i(TAG, "Network${connectID}：onConnect")
        isConnect = true
        callbacks.forEach { it.onConnect() }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        val networkID = network.toString().toInt()

        if (networkID >= connectID) {
            Log.i(TAG, "Network$networkID：unConnect")
            isConnect = false
            networkType = NetworkType.Other
            callbacks.forEach { it.unConnect() }
        }
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                if (networkType != NetworkType.Wifi) {
                    Log.i(TAG, "networkTypeChange: wifi:$network")
                    networkType = NetworkType.Wifi
                    callbacks.forEach { it.networkTypeChange(NetworkType.Wifi) }
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                if (networkType != NetworkType.Network) {
                    Log.i(TAG, "networkTypeChange: network:$network")
                    networkType = NetworkType.Network
                    callbacks.forEach { it.networkTypeChange(NetworkType.Network) }
                }
            } else {
                if (networkType != NetworkType.Other) {
                    Log.i(TAG, "networkTypeChange: other:$network")
                    networkType = NetworkType.Other
                    callbacks.forEach { it.networkTypeChange(NetworkType.Other) }
                }
            }
        }
    }

    fun bindCallback(callback: NetworkConnectCallback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    fun unbindCallback(callback: NetworkConnectCallback) {
        if (callbacks.contains(callback)) {
            callbacks.remove(callback)
        }
    }

    fun isConnect() = isConnect

    fun networkType() = networkType

}