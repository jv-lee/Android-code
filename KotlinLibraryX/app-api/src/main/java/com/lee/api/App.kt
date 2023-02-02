package com.lee.api

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.lee.api.startup.Startup
import com.lee.library.base.BaseApplication
import com.lee.library.utils.LogUtil

/**
 * 监听应用前后台切换 "无法监听到应用被杀死"
 * @author jv.lee
 * @date 2021/10/29
 */
class App : BaseApplication() {

    override fun init() {
        Startup.initialize(context = this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
    }

    override fun unInit() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(processLifecycleObserver)
    }

    /**
     * 只能监听应用创建、在前台、在后台
     * 无法监听应用被杀死
     */
    private val processLifecycleObserver =
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> LogUtil.i("createApplication")
                Lifecycle.Event.ON_START -> LogUtil.i("startApplication")
                Lifecycle.Event.ON_RESUME -> LogUtil.i("resumeApplication")
                Lifecycle.Event.ON_PAUSE -> LogUtil.i("pauseApplication")
                Lifecycle.Event.ON_STOP -> LogUtil.i("stopApplication")
                else -> {}
            }
        }
}