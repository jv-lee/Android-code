package com.lee.library.base

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.fragment.findNavController

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description navigationFragment 处理show/hide未响应生命周期问题
 */
abstract class BaseNavigationFragment(val layoutId: Int) : BaseFragment(layoutId) {

    private var isResume = false
    private var isStop = true

    private val navigationLifecycleOwner = object : LifecycleOwner {
        var registry: LifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle() = registry
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.getViewLifecycleOwner().lifecycle.run {
            addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    // 页面已失去焦点隐藏后不处理生命周期事件，重新获取焦点fragmentResume状态变更后处理事件
                    if (isStop && event != Lifecycle.Event.ON_DESTROY) {
                        return
                    }

                    // 生命周期事件通知
                    navigationLifecycleOwner.registry.handleLifecycleEvent(event)

                    // 解除事件监听
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        removeObserver(this)
                    }
                }
            })
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onPause()
            onStop()
            handleFragmentStop()
        } else {
            onStart()
            onResume()
            handleFragmentResume()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentClass = this::class.java.simpleName
        val destination: String
        val parentClass: String

        if (parentFragment != null) {
            destination =
                requireParentFragment().findNavController().currentDestination.toString()
            parentClass = requireParentFragment()::class.java.simpleName
        } else {
            destination = findNavController().currentDestination.toString()
            parentClass = ""
        }

        if (destination.contains(currentClass) || destination.contains(parentClass)) {
            handleFragmentResume()
        } else {
            postFragmentStop()
        }
    }

    override fun onStop() {
        super.onStop()
        handleFragmentStop()
    }

    open fun onFragmentResume() {
        isResume = true
        isStop = false
        handleViewLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    open fun onFragmentStop() {
        isResume = false
        isStop = true
    }

    private fun handleFragmentResume() {
        if (!isResume) {
            onFragmentResume()
        }
    }

    private fun handleFragmentStop() {
        handleViewLifecycleEvent(Lifecycle.Event.ON_STOP)
        if (!isStop) {
            onFragmentStop()
        }
    }

    private fun postFragmentStop() {
        postViewLifecycleEvent(Lifecycle.Event.ON_STOP)
        if (!isStop) {
            onFragmentStop()
        }
    }

    override fun getViewLifecycleOwner(): LifecycleOwner {
        return navigationLifecycleOwner
    }

    private fun getViewLifecycleRegistry(): LifecycleRegistry? {
        return super.getViewLifecycleOwner().lifecycle as? LifecycleRegistry
    }

    private fun handleViewLifecycleEvent(@NonNull event: Lifecycle.Event) {
        getViewLifecycleRegistry()?.handleLifecycleEvent(event)
    }

    private fun postViewLifecycleEvent(@NonNull event: Lifecycle.Event) {
        val lifecycle = getViewLifecycleRegistry()
        view?.post { lifecycle?.handleLifecycleEvent(event) }
    }

}