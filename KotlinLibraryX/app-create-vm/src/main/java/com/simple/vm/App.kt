package com.simple.vm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @author jv.lee
 * @date 2021/8/19
 * @description
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}