@file:Suppress("LeakingThis")

package com.lee.library.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lee.library.R

/**
 * 通用AlertDialogFragment 基础类 带Alert缩放动画
 * @author jv.lee
 * @date 2020/9/21
 */
abstract class BaseVMAlertFragment<V : ViewDataBinding, VM : ViewModel>(
    resId: Int,
    isCancel: Boolean = true,
    isFullWindow: Boolean = true
) :
    BaseVMDialogFragment<V, VM>(resId, isCancel, isFullWindow) {
    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseAlertDialog)
    }
}