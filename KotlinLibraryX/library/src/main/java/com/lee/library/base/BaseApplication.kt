package com.lee.library.base

import android.app.Application
import androidx.multidex.MultiDexApplication

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description
 */
abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        ApplicationExtensions.app = this
        init()
    }

    override fun onTerminate() {
        super.onTerminate()
        unInit()
    }

    protected abstract fun init()
    protected abstract fun unInit()

}

object ApplicationExtensions {
    lateinit var app: Application
}