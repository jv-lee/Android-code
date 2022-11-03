package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lee.library.extensions.getVmClass

/**
 * 封装ViewModel\DataBinding SheetFragment通用基类
 * @author jv.lee
 */
abstract class BaseVMSheetFragment<V : ViewDataBinding, VM : ViewModel>(
    var layoutId: Int,
    isFullWindow: Boolean = false,
    behaviorState: Int = BottomSheetBehavior.STATE_EXPANDED,
    peekHeight: Int = -1
) : BaseSheetFragment(
    isFullWindow = isFullWindow,
    behaviorState = behaviorState,
    peekHeight = peekHeight
) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        // 设置viewBinding
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    override fun getBehavior(): BottomSheetBehavior<*>? {
        return BottomSheetBehavior.from(binding.root.parent as View)
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
