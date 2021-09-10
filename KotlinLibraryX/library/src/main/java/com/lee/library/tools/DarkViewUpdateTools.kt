package com.lee.library.tools

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author jv.lee
 * @date 2021/6/8
 * @description
 */
object DarkViewUpdateTools {

    private val viewCallbackMap by lazy { HashMap<LifecycleOwner, ViewCallback>() }
    private var mIsDark = DarkModeTools.get().isDarkTheme()

    fun bindViewCallback(owner: LifecycleOwner, view: ViewCallback) {
        viewCallbackMap[owner] = view
        owner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                viewCallbackMap.remove(owner)
                owner.lifecycle.removeObserver(this)
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