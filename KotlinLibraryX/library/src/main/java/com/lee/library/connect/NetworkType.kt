package com.lee.library.connect

/**
 * 网络类型密封类
 * @author jv.lee
 * @date 2022/11/2
 */
sealed class NetworkType {
    object Wifi : NetworkType()
    object Network : NetworkType()
    object Other : NetworkType()
}
