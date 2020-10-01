package com.lee.library.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.lee.library.R
import com.lee.library.dialog.core.BaseAlertDialog

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 警告提示dialog
 */
open class WarnDialog(context: Context, private var titleText: String = "") :
    BaseAlertDialog(context, false) {

    public override fun buildViewId(): Int {
        return R.layout.layout_dialog_warm
    }

    public override fun bindView() {
        (findViewById<View>(R.id.tv_title) as TextView).text = titleText

        findViewById<View>(R.id.tv_confirm).setOnClickListener { v: View? ->
            if (confirmListener != null) {
                confirmListener?.onConfirm()
            } else {
                dismiss()
            }
        }
    }
}