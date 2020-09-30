package com.lee.library.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description 通用AlertDialogFragment 基础类 带Alert缩放动画
 */
abstract class BaseAlertDialogFragment<V : ViewDataBinding, VM : ViewModel>(
    resId: Int,
    isCancel: Boolean = true
) :
    BaseDialogFragment<V, VM>(resId, isCancel) {
    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseAlertDialog)
    }
}