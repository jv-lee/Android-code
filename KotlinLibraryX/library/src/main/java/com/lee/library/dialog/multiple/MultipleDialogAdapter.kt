package com.lee.library.dialog.multiple

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 多个dialog显示控制适配器 按顺序执行dialog显示逻辑
 * @author jv.lee
 * @date 2021/8/23
 */
internal class MultipleDialogAdapter(
    private val fragmentManager: FragmentManager,
    private val nextCall: () -> Unit
) {

    fun <T> switchShowType(dialog: T) {
        if (dialog is DialogFragment) {
            // 显示fragmentDialog
            showDialogFragment(dialog)
        } else if (dialog is Dialog) {
            // 显示dialog
            showDialog(dialog)
        }
    }

    private fun showDialog(dialog: Dialog) {
        dialog.setOnDismissListener {
            dialog.setOnDismissListener(null)
            nextCall.invoke()
        }
        dialog.show()
    }

    private fun showDialogFragment(
        dialogFragment: DialogFragment
    ) {
        dialogFragment.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dialogFragment.lifecycle.removeObserver(this)
                    nextCall.invoke()
                }
            }
        })

        dialogFragment.show(fragmentManager, dialogFragment::class.java.simpleName)
    }
}