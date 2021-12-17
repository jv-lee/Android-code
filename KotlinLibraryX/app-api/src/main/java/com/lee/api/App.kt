package com.lee.api

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.lee.library.base.BaseApplication
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @date 2021/10/29
 * @description 监听应用前后台切换 "无法监听到应用被杀死"
 */
class App : BaseApplication() {

    override fun init() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
    }

    override fun unInit() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(processLifecycleObserver)
    }

    private val processLifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun createApplication() {
            LogUtil.i("createApplication")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun startApplication() {
            LogUtil.i("startApplication")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun resumeApplication() {
            LogUtil.i("resumeApplication")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun pauseApplication() {
            LogUtil.i("pauseApplication")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stopApplication() {
            LogUtil.i("stopApplication")
        }
    }
}