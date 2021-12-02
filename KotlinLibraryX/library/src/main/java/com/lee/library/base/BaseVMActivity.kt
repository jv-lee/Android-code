package com.lee.library.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.getVmClass
import com.lee.library.extensions.toast
import com.lee.library.mvvm.base.BaseViewModel

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
abstract class BaseVMActivity<V : ViewDataBinding, VM : BaseViewModel>(var layoutId: Int) :
    BaseActivity() {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置viewBinding
        binding = DataBindingUtil.setContentView(this, layoutId)

        //设置viewModel
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }
        super.onCreate(savedInstanceState)
    }

}