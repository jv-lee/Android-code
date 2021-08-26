package com.lee.basedialog.intercept

import com.lee.basedialog.fragment.BaseSheetDialogFragmentImpl
import com.lee.library.dialog.core.DialogActionCall
import com.lee.library.dialog.intercept.DialogCreateConfig
import com.lee.library.dialog.intercept.DialogIntercept

/**
 * @author jv.lee
 * @data 2021/8/26
 * @description
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

        dialog.actionCall = object : DialogActionCall {
            override fun confirm() {
                item.isShow = true
                showCall.invoke()
            }

            override fun cancel() {
                item.isShow = false
                showCall.invoke()
            }
        }
    }
}