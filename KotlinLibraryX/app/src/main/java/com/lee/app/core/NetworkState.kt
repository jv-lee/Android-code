package com.lee.app.core

/**
 *
 * @author jv.lee
 * @date 2020-03-08
 *
 */
data class NetworkState(
    var code: Int,
    var type: Int,
    var isConnection: Boolean
)