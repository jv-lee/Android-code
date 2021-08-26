package com.lee.basedialog.intercept

import com.lee.library.dialog.ChoiceDialog
import com.lee.library.dialog.core.CancelListener
import com.lee.library.dialog.core.ConfirmListener
import com.lee.library.dialog.intercept.DialogCreateConfig
import com.lee.library.dialog.intercept.DialogIntercept

/**
 * @author jv.lee
 * @data 2021/8/26
 * @description
 */
class ThreeDialogIntercept : DialogIntercept<DialogCreateConfig>() {

    private var dialog: ChoiceDialog? = null

    override fun intercept(item: DialogCreateConfig) {
        if (dialog == null) {
            dialog = ChoiceDialog(item.context)
            dialog?.setTitle("点击确认进入下一级弹窗")
        }

        //当前dialog是否显示
        if (item.isShow) {
            dialog?.show()
        }

        //取消后进入下一级弹窗判断 显示状态关闭
        dialog?.cancelListener = CancelListener {
            dialog?.dismiss()
            item.isShow = false
            super.intercept(item)
        }

        //确认后进入下一级弹窗判断 显示状态打开
        dialog?.confirmListener = ConfirmListener {
            dialog?.dismiss()
            item.isShow = true
            super.intercept(item)
        }
    }
}