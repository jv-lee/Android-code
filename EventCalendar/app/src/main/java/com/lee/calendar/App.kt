package com.lee.calendar

import android.app.Application
import com.squareup.leakcanary.LeakCanary


/**
 * @author jv.lee
 * @date 2021/1/7
 * @description
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        LeakCanary.enableDisplayLeakActivity(this)
    }

}