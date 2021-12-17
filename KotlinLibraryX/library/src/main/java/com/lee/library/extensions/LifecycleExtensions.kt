package com.lee.library.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author jv.lee
 * @date 2021/8/26
 * @description
 */
inline fun Lifecycle.destroy(crossinline call: () -> Unit) {
    addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            removeObserver(this)
            call()
        }
    })
}