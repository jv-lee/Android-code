package com.lee.basedialog.intercept

import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.dialog.intercept.DialogCreateConfig
import com.lee.library.dialog.intercept.DialogIntercept

/**
 *
 * @author jv.lee
 * @date 2021/8/26
 */
class FourDialogIntercept : DialogIntercept<DialogCreateConfig>() {

    private val dialog by lazy { BaseSheetDialogFragmentImpl() }

    override fun intercept(item: DialogCreateConfig) {
        //当前dialog是否显示
        if (item.isShow) {
            item.fragmentManager?.let { dialog.show(it, "for") }
        }

        val showCall = {
            super.intercept(item)
        }

        dialog.onConfirm = {
            item.isShow = true
            showCall.invoke()
        }
        dialog.onCancel = {
            item.isShow = false
            showCall.invoke()
        }
    }
}