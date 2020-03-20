package com.lee.library.base

import android.app.Application
import android.content.Context

/**
 * @author jv.lee
 * @date 2020/3/20
 * @description
 */
abstract class BaseApplication : Application() {

    companion object {
        private lateinit var sContext: Context

        fun getContext(): Context {
            return sContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sContext = this
        init()
    }

    override fun onTerminate() {
        super.onTerminate()
        unInit()
    }

    protected abstract fun init()
    protected abstract fun unInit()

}