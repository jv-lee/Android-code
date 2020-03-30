package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
abstract class BaseNavigationFragment<V : ViewDataBinding, VM : ViewModel>(
    layoutId: Int,
    vm: Class<VM>?
) : BaseFragment<V, VM>(layoutId, vm) {

    private var lastView: View? = null // 记录上次创建的view
    private var isNavigationViewInit = false // 记录是否初始化view

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置viewBinding
        if (lastView == null) {
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            lastView = binding.root
        }
        return lastView
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

}