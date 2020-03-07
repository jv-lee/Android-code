package com.lee.library.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lee.library.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 */
abstract class BaseSheetFragment<V : ViewDataBinding, VM : ViewModel>(
    var layoutId: Int,
    var vm: Class<VM>?
) : BottomSheetDialogFragment() , CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var mBehavior: BottomSheetBehavior<*>? = null
    private var isVisibleUser = false
    private var isVisibleView = false
    private var fistVisible = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //设置viewBinding
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        getBehavior()!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun getBehavior(): BottomSheetBehavior<*>? {
        if (mBehavior == null) {
            mBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        }
        return mBehavior
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置viewModel
        if (vm != null) viewModel = ViewModelProviders.of(this).get<VM>(vm!!)
        bindData(savedInstanceState)
        bindView()
        isVisibleView = true
        if (isVisibleUser && fistVisible) {
            fistVisible = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isVisibleUser = true
            onFragmentResume()
            //首次用户可见 开始加载数据
            if (isVisibleView && isVisibleUser && fistVisible) {
                fistVisible = false
            }
        } else {
            isVisibleUser = false
            onFragmentPause()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onDetach() {
        super.onDetach()
        cancel()
    }

    open fun onFragmentResume() {}

    open fun onFragmentPause() {}

    /**
     * 设置加载数据等业务操作
     *
     * @param savedInstanceState 重置回调参数
     */
    protected abstract fun bindData(savedInstanceState: Bundle?)

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

}
