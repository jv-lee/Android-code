package com.lee.library.tools

import androidx.lifecycle.*

/**
 *
 * @author jv.lee
 * @date 2021/6/8
 *
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