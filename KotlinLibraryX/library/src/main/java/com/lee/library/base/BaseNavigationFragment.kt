package com.lee.library.base

import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description navigationFragment 处理show/hide未响应生命周期问题
 */
abstract class BaseNavigationFragment(val layoutId: Int) : BaseFragment(layoutId) {

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onPause()
            onStop()
            handleViewLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            handleViewLifecycleEvent(Lifecycle.Event.ON_STOP)
        } else {
            onStart()
            onResume()
            handleViewLifecycleEvent(Lifecycle.Event.ON_START)
            handleViewLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    private fun getViewLifecycleRegistry(): LifecycleRegistry? {
        return viewLifecycleOwner.lifecycle as? LifecycleRegistry
    }

    private fun handleViewLifecycleEvent(@NonNull event: Lifecycle.Event) {
        getViewLifecycleRegistry()?.handleLifecycleEvent(event)
    }

}