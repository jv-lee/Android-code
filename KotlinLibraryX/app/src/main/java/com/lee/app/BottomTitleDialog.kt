package com.lee.app

import android.content.Context
import android.widget.TextView
import com.lee.library.dialog.core.BaseBottomDialog

/**
 * @author jv.lee
 * @date 2020/9/10
 * @description
 */
class BottomTitleDialog(context: Context) : BaseBottomDialog(context,260) {

    private var tvTitle: TextView? = null

    override fun buildViewId(): Int {
        return R.layout.dialog_bottom_title
    }

    override fun bindView() {
        tvTitle = findViewById(R.id.tv_title)
    }

    fun setContentTitle(text: String) {
        tvTitle?.text = text
    }
}