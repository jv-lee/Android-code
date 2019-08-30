package com.lee.library.base

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel>(var layoutId: Int, var vm: Class<VM>?) : Fragment() {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var isVisibleUser = false
    private var isVisibleView = false
    private var fistVisible = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    /**
     * 使用page 多fragment时 懒加载
     */
    open fun lazyLoad() {}

    fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, message, duration).show()
    }
}