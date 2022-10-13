package com.lee.library.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.lee.library.R
import com.lee.library.dialog.core.BaseAlertDialog

/**
 * 警告提示dialog
 * @author jv.lee
 * @date 2020-03-07
 */
class WarnDialog(context: Context) : BaseAlertDialog(context, R.layout.layout_dialog_warn, false) {

    public override fun bindView() {
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            onConfirm ?: dismiss()
            onConfirm?.invoke()
        }
    }

    override fun setTitle(titleId: Int) {
        findViewById<TextView>(R.id.tv_title).setText(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        findViewById<TextView>(R.id.tv_title).text = title
    }
}