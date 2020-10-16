package com.lee.library.dialog

import android.content.Context
import com.lee.library.R
import com.lee.library.dialog.core.BaseTranslucentDialog

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description
 */
class LoadingDialog(context: Context) : BaseTranslucentDialog(context, false) {
    override fun buildViewId(): Int {
        return R.layout.layout_dialog_loading
    }

    override fun bindView() {}
}