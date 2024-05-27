@file:Suppress("LeakingThis")

package com.lee.library.base

import androidx.viewbinding.ViewBinding
import com.lee.library.R

/**
 * AlertDialogFragment通用基类，带Alert缩放动画
 */
abstract class BaseBindingAlertFragment<VB : ViewBinding>(
    isCancel: Boolean = true,
    isFullWindow: Boolean = true
) :
    BaseBindingDialogFragment<VB>(isCancel = isCancel, isFullWindow = isFullWindow) {

    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseAlertDialog)
    }
}