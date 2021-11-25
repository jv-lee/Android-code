package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OneShotPreDrawListener
import androidx.fragment.app.Fragment

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseFragment(private val resourceId: Int? = 0) : Fragment() {

    private var fistVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        bindView()
        bindData()
        (view.parent as? ViewGroup)?.apply {
            OneShotPreDrawListener.add(this) {
                startPostponedEnterTransition()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (fistVisible) {
            fistVisible = false
            lazyLoad()
        }
    }

    override fun onPause() {
        super.onPause()
        if (requireActivity().isFinishing) {
            dispose()
        }
    }

    /**
     * 解决fragment中 onDestroy等函数回调过慢时 使用该方法解除引用
     */
    open fun dispose() {
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (resourceId == null || resourceId == 0) throw RuntimeException("fragment createView() not override && constructor params resourceId == 0")
        return inflater.inflate(resourceId, container, false)
    }

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     */
    protected abstract fun bindData()

    /**
     * 使用page 多fragment时 懒加载
     */
    open fun lazyLoad() {}

    private fun getChildClassName(): String {
        return javaClass.simpleName
    }

}