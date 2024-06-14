@file:Suppress("LeakingThis")

package com.lee.library.base

import androidx.viewbinding.ViewBinding
import com.lee.library.R

/**
 * 通过反射实现binding注入 baseAlertFragment
 * @author jv.lee
 * @date 2024/6/6
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