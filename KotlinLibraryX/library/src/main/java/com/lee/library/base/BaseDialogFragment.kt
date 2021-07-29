package com.lee.library.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lee.library.dialog.core.setBackDismiss
import com.lee.library.dialog.core.setFullWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseDialogFragment(
    private val resourceId: Int? = 0,
    private val isCancel: Boolean = true,
    private val isFullWindow: Boolean = true
) :
    DialogFragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var fistVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setBackDismiss(isCancel)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //全屏显示
        if (isFullWindow) dialog?.setFullWindow(context)
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

    /**
     * 初始化参数配置
     */
    open fun intentParams(arguments: Bundle?, savedInstanceState: Bundle?) {

    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (resourceId == null || resourceId == 0) throw RuntimeException("fragment createView() not override && constructor params resourceId == 0")
        return inflater.inflate(resourceId, container, false)
    }

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

    /**
     * 创建ViewModel
     */
    protected fun <T : ViewModel> createViewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }
}