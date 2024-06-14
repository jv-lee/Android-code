package com.lee.library.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

/**
 * navigationFragment 处理show/hide未响应生命周期问题
 * @author jv.lee
 * @date 2020/3/30
 */
abstract class BaseNavigationFragment(val layoutId: Int = 0) : BaseFragment(layoutId) {

    private var isResume = false
    private var isStop = true

    abstract fun LifecycleCoroutineScope.bindData()

    override fun bindData() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.bindData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) onStop() else onResume()
    }

    override fun onResume() {
        super.onResume()
        val currentClass = javaClass.simpleName
        val destination: String
        val parentClass: String

        if (parentFragment != null) {
            destination =
                requireParentFragment().findNavController().currentDestination.toString()
            parentClass = requireParentFragment().javaClass.simpleName
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
        // stop事件在最开始更新生命周期状态-> home离开应用后所有fragment会回调onStop，再次进入时不在最前台的fragment需要重新调用stop覆盖resume状态
        handleViewLifecycleEvent(Lifecycle.Event.ON_STOP)
        if (!isStop) {
            onFragmentStop()
        }
    }

    private fun postFragmentStop() {
        // stop事件在最开始更新生命周期状态-> home离开应用后所有fragment会回调onStop，再次进入时不在最前台的fragment需要重新调用stop覆盖resume状态
        postViewLifecycleEvent(Lifecycle.Event.ON_STOP)
        if (!isStop) {
            onFragmentStop()
        }
    }

    private fun getViewLifecycleRegistry(): LifecycleRegistry? {
        return super.getViewLifecycleOwner().lifecycle as? LifecycleRegistry
    }

    private fun handleViewLifecycleEvent(event: Lifecycle.Event) {
        getViewLifecycleRegistry()?.handleLifecycleEvent(event)
    }

    private fun postViewLifecycleEvent(event: Lifecycle.Event) {
        val lifecycle = getViewLifecycleRegistry()
        view?.post { lifecycle?.handleLifecycleEvent(event) }
    }
}