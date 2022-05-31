package com.lee.library.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.getVmClass

/**
 *
 * @author jv.lee
 * @date 2019-08-15

 */
abstract class BaseVMActivity<V : ViewDataBinding, VM : ViewModel>(var layoutId: Int) :
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