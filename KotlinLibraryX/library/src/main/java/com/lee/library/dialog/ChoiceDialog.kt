package com.lee.library.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.lee.library.R
import com.lee.library.dialog.core.BaseAlertDialog

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 取消确认选择框
 */
class ChoiceDialog(context: Context) : BaseAlertDialog(context, R.layout.layout_dialog_choice,false) {

    public override fun bindView() {
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            confirmListener ?: dismiss()
            confirmListener?.onConfirm()
        }
        findViewById<View>(R.id.tv_cancel).setOnClickListener {
            cancelListener ?: dismiss()
            cancelListener?.onCancel()
        }
    }

    override fun setTitle(titleId: Int) {
        findViewById<TextView>(R.id.tv_title).setText(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        findViewById<TextView>(R.id.tv_title).text = title
    }

}