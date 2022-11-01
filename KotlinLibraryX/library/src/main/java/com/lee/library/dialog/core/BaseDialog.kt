@file:Suppress("LeakingThis")

package com.lee.library.dialog.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.lee.library.dialog.extensions.setBackDismiss
import com.lee.library.dialog.extensions.setFullWindow

/**
 * Dialog通用基类
 * @author jv.lee
 * @date 2020/9/10
 */
abstract class BaseDialog constructor(
    private val mContext: Context,
    theme: Int,
    layoutId: Int,
    isCancel: Boolean = true
) :
    Dialog(mContext, theme) {

    init {
        setContentView(layoutId)
        setFullWindow()
        setBackDismiss(isCancel)
        bindView()
        bindData()
    }

    override fun show() {
        if (mContext is Activity) {
            if (mContext.isFinishing) {
                return
            }
        }
        super.show()
    }

    override fun dismiss() {
        if (mContext is Activity) {
            if (mContext.isFinishing) {
                return
            }
        }
        super.dismiss()
    }

    override fun onBackPressed() {
        if (isShowing) {
            dismiss()
            return
        }
        super.onBackPressed()
    }

    /**
     * 绑定view
     */
    protected abstract fun bindView()

    /**
     * 绑定数据
     */
    protected open fun bindData() {}

}
