package com.lee.library.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
open abstract class BaseNavigationFragment<V : ViewDataBinding, VM : ViewModel>(
    layoutId: Int,
    vm: Class<VM>?
) : BaseFragment<V, VM>(layoutId, vm) {

    private var isNavigationViewInit = false // 记录是否初始化view

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //设置viewBinding
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isNavigationViewInit) {
            //设置viewModel
            if (vm != null) viewModel = ViewModelProviders.of(this).get<VM>(vm!!)
            intentParams(arguments, savedInstanceState)
            bindView()
            bindData()
            isNavigationViewInit = true
        }
    }

    /**
     * 显示底部导航栏
     */
    open fun showNavigation() {
        if (activity is BaseNavigationActivity<*, *>) {
            (activity as BaseNavigationActivity<*, *>).showView()
        }
    }

    /**
     * 隐藏底部导航栏
     */
    open fun hideNavigation() {
        if (activity is BaseNavigationActivity<*, *>) {
            (activity as BaseNavigationActivity<*, *>).hideView()
        }
    }

    open fun setWebBackEvent(web: WebView) {
        web.isFocusable = true
        web.isFocusableInTouchMode = true
        web.requestFocus()
        web.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, i: Int, keyEvent: KeyEvent?): Boolean {
                if (web.canGoBack()) {
                    web.goBack()
                    return true
                }
                return false
            }

        })
    }

}