package com.lee.basedialog.intercept

import com.lee.basedialog.fragment.BaseDialogFragmentImpl
import com.lee.library.dialog.intercept.DialogCreateConfig
import com.lee.library.dialog.intercept.DialogIntercept
import com.lee.library.extensions.destroy

/**
 * @author jv.lee
 * @date 2021/8/26
 * @description
 */
class OneDialogIntercept : DialogIntercept<DialogCreateConfig>() {

    private val dialog by lazy { BaseDialogFragmentImpl() }

    override fun intercept(item: DialogCreateConfig) {

        //当前dialog是否显示
        if (item.isShow) {
            item.fragmentManager?.let { dialog.show(it, "one") }
        }

        //当前dialog关闭后 下一个dialog直接显示
        dialog.lifecycle.destroy {
            item.isShow = true
            super.intercept(item)
        }
    }
}