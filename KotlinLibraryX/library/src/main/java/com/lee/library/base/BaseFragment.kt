package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.library.extensions.getVmClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
open abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel>(
    var layoutId: Int
) : Fragment()
    , CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var fistVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置viewBinding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //设置viewModel
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }
        intentParams(arguments, savedInstanceState)
        bindView()
        bindData()
    }

    override fun onResume() {
        super.onResume()
        if (fistVisible) {
            fistVisible = false
            lazyLoad()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onDetach() {
        super.onDetach()
        cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * 初始化参数传递
     */
    open fun intentParams(arguments: Bundle?, savedInstanceState: Bundle?) {}

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     *
     */
    protected abstract fun bindData()


    /**
     * 使用page 多fragment时 懒加载
     */
    open fun lazyLoad() {}

    fun Fragment.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, message, duration).show()
    }

    private fun getChildClassName(): String {
        return javaClass.simpleName
    }

}