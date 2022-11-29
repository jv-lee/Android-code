package com.lee.app

import com.lee.library.base.BaseApplication
import com.lee.library.connect.NetworkConnectManager

/**
 *
 * @author jv.lee
 * @date 2020-03-07
 */
class App : BaseApplication() {

    override fun init() {
        NetworkConnectManager.instance.init(this)
    }

    override fun unInit() {
    }
}
