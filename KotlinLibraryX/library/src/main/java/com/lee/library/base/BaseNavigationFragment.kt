package com.lee.library.base

import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
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

    private fun getViewLifecycleRegistry(): LifecycleRegistry? {
        return viewLifecycleOwner.lifecycle as? LifecycleRegistry
    }

    private fun handleViewLifecycleEvent(@NonNull event: Lifecycle.Event) {
        getViewLifecycleRegistry()?.handleLifecycleEvent(event)
    }

    private fun postViewLifecycleEvent(@NonNull event: Lifecycle.Event) {
        val lifecycle = getViewLifecycleRegistry()
        view?.post { lifecycle?.handleLifecycleEvent(event) }
    }

}