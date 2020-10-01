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
    cancel: Boolean = true
) :
    BaseDialog(context, R.style.BaseAlertDialog, cancel) {

    @JvmField
    var confirmListener: ConfirmListener? = null

    @JvmField
    var cancelListener: CancelListener? = null
}