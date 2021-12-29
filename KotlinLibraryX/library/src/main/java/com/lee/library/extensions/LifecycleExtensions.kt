package com.lee.library.extensions

import androidx.lifecycle.*

/**
 * @author jv.lee
 * @date 2021/8/26
 * @description
 */
inline fun Lifecycle.destroy(crossinline call: () -> Unit) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                removeObserver(this)
                call()
            }
        }
    })
}