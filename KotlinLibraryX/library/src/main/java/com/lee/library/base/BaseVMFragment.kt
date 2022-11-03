package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.getVmClass

/**
 * 封装ViewModel\DataBinding Fragment通用基类
 * @author jv.lee
 * @date 2019/8/16.
 */
abstract class BaseVMFragment<V : ViewDataBinding, VM : ViewModel>(var layoutId: Int) :
    BaseFragment() {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        // 设置viewBinding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 设置viewModel
        try {
            viewModel = ViewModelProvider(this)[getVmClass(this)]
        } catch (_: Exception) {
        }
        super.onViewCreated(view, savedInstanceState)
    }
}