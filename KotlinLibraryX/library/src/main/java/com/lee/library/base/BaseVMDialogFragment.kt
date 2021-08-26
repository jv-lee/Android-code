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
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseVMDialogFragment<V : ViewDataBinding, VM : ViewModel>(
    private val layoutId: Int,
    isCancel: Boolean = true,
    isFullWindow: Boolean = true
) :
    BaseDialogFragment(isCancel = isCancel, isFullWindow = isFullWindow) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        //设置viewBinding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }
        super.onViewCreated(view, savedInstanceState)
    }


}