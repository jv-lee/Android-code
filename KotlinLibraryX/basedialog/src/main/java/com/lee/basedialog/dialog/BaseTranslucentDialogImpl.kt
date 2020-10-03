package com.lee.basedialog.dialog

import android.content.Context
import com.lee.basedialog.R
import com.lee.library.dialog.core.BaseTranslucentDialog

/**
 * @author jv.lee
 * @date 2020/9/30
 * @description
 */
class BaseTranslucentDialogImpl(context: Context) : BaseTranslucentDialog(context) {
    override fun buildViewId(): Int {
        return R.layout.dialog_translucent
    }

    override fun bindView() {

    }

}