package com.lee.library.lifecycle

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author jv.lee
 * @date 2020/12/1
 * @description
 */
interface ViewLifecycle : LifecycleObserver {

    fun bindLifecycle(context: Context?) {
        context ?: return
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifecycleCancel()

}