package com.lee.library.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lee.library.R
import com.lee.library.extensions.getVmClass
import com.lee.library.utils.SizeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 */
abstract class BaseSheetFragment<V : ViewDataBinding, VM : ViewModel>(
    var layoutId: Int,
    var isFullWindow: Boolean = false,
    var behaviorState: Int = BottomSheetBehavior.STATE_EXPANDED,
    var peekHeight: Int = -1
) : BottomSheetDialogFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置viewBinding
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (isFullWindow) {
            val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        getBehavior()?.peekHeight = SizeUtil.dp2px(context, peekHeight.toFloat())
        getBehavior()?.state = behaviorState
    }

    open fun getBehavior(): BottomSheetBehavior<*>? {
        return BottomSheetBehavior.from(binding.root.parent as View)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置viewModel
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }
        intentParams(arguments, savedInstanceState)
        bindView()
        bindData()
    }

    open fun intentParams(arguments: Bundle?, savedInstanceState: Bundle?) {}

    @ExperimentalCoroutinesApi
    override fun onDetach() {
        super.onDetach()
        cancel()
    }

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     *
     * @param savedInstanceState 重置回调参数
     */
    protected abstract fun bindData()


}
