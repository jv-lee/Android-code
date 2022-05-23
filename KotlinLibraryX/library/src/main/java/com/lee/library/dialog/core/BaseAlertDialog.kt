package com.lee.library.dialog.core

import android.content.Context
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 自定义dialog 超类
 */
abstract class BaseAlertDialog(
    context: Context,
    layoutId: Int,
    isCancel: Boolean = true
) :
    BaseDialog(context, R.style.BaseAlertDialog, layoutId, isCancel) {

    @JvmField
    var onConfirm: (()->Unit)? = null

    @JvmField
    var onCancel: (()->Unit)? = null
}