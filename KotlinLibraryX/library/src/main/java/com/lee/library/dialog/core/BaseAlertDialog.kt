package com.lee.library.dialog.core

import android.content.Context
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 自定义dialog 超类
 */
abstract class BaseAlertDialog(context: Context, cancel: Boolean = true) :
    BaseDialog(context, R.style.BaseAlertDialog, cancel) {

    @JvmField
    protected var cancelListener: CancelListener? = null

    @JvmField
    protected var confirmListener: ConfrimListener? = null

    fun setCancelListener(cancelListener: CancelListener) {
        this.cancelListener = cancelListener
    }

    fun setConfirmListener(confirmListener: ConfrimListener) {
        this.confirmListener = confirmListener
    }

}