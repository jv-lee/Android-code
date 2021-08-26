package com.lee.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.getVmClass
import com.lee.library.mvvm.base.BaseViewModel

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
abstract class BaseVMNavigationFragment<V : ViewDataBinding, VM : BaseViewModel>(layoutId: Int) :
    BaseVMFragment<V, VM>(layoutId) {

    private var isNavigationViewInit = false // 记录是否初始化view

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //设置viewBinding
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        return super.createView(inflater, container)
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
            try {
                viewModel = ViewModelProvider(this).get(getVmClass(this))
            } catch (e: Exception) {
            }
            bindView()
            bindData()
            initFailedViewModel()
            isNavigationViewInit = true
        }
    }

}