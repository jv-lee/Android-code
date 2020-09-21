package com.lee.library.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description
 */
abstract class BaseAlertDialogFragment<V : ViewDataBinding, VM : ViewModel>(var resId: Int) :
    BaseDialogFragment<V, VM>(layoutId = resId) {
    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseAlertDialog)
    }
}