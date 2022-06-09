package com.lee.library.tools

import androidx.lifecycle.*

/**
 * 深色主题ui更新帮助类 - activity/fragment 实现 [ViewCallback] 接口，
 * 通过 [DarkViewUpdateTools.bindViewCallback] 绑定生命周期监听view更新状态来实现深色主题切换
 * 组件间可主动调用 [DarkViewUpdateTools.notifyUiMode] 方法来实现全局通知ui更新
 * @author jv.lee
 * @date 2021/6/8
 */
object DarkViewUpdateTools {

    private val viewCallbackMap by lazy { HashMap<LifecycleOwner, ViewCallback>() }
    private var mIsDark = DarkModeTools.get().isDarkTheme()

    fun bindViewCallback(owner: LifecycleOwner, view: ViewCallback) {
        viewCallbackMap[owner] = view
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    viewCallbackMap.remove(owner)
                    owner.lifecycle.removeObserver(this)
                }
            }
        })
    }

    fun notifyUiMode() {
        val isDark = DarkModeTools.get().isDarkTheme()
        if (mIsDark == isDark) return
        for (entry in viewCallbackMap) {
            if (entry.key.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                entry.value.updateDarkView()
            }
        }
        mIsDark = isDark
    }

    interface ViewCallback {
        fun updateDarkView()
    }

}