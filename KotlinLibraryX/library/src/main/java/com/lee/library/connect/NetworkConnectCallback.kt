package com.lee.library.connect

/**
 * 网络连接状态监听回调接口
 * @author jv.lee
 * @date 2022/11/2
 */
interface NetworkConnectCallback {
    fun onConnect()
    fun unConnect()
    fun networkTypeChange(type: NetworkType)
}