package com.lee.library.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import java.util.*

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseDialogFragment<V : ViewDataBinding, VM : ViewModel>(var layoutId: Int, var vm: Class<VM>?) :
    DialogFragment() {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var isVisibleUser = false
    private var isVisibleView = false
    private var fistVisible = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Objects.requireNonNull<Window>(dialog.window)
            .setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        this.dialog.setCancelable(false)
        this.dialog.setCanceledOnTouchOutside(false)
        //设置viewBinding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
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
            lazyLoad()
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
                lazyLoad()
            }
        } else {
            isVisibleUser = false
            onFragmentPause()
        }
    }

    protected fun onFragmentResume() {}

    protected fun onFragmentPause() {}


    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

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

    /**
     * 使用page 多fragment时 懒加载
     */
    protected fun lazyLoad() {}
}