package com.lee.library.dialog.core

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

/**
 * @author jv.lee
 * @data 2021/8/23
 * @description 多Dialog展示任务工具类
 * 通过 addAction添加dialog展示任务 (后进先出原则)
 * 最终通过nextShow开始所有dialog任务顺序展示
 */
class MultipleDialogTask(private val fragmentManager: FragmentManager) {

    private val stack: Stack<DialogElement> = Stack()

    fun addAction(dialog: DialogFragment, action: () -> Boolean = { true }): MultipleDialogTask {
        stack.push(DialogElement(dialog, action))
        return this
    }

    fun addAction(dialog: Dialog, action: () -> Boolean = { true }): MultipleDialogTask {
        stack.push(DialogElement(dialog, action))
        return this
    }

    fun nextShow() {
        if (stack.empty()) return

        kotlin.runCatching {
            stack.pop()
                ?.takeIf {
                    it.action.invoke()
                }?.run {
                    switchShowType(dialog)
                } ?: kotlin.run {
                nextShow()
            }
        }.onFailure {
            nextShow()
        }

    }

    private fun switchShowType(dialog: Any) {
        if (dialog is DialogFragment) {
            //显示fragmentDialog
            showDialogFragment(dialog)
        } else if (dialog is Dialog) {
            //显示dialog
            showDialog(dialog)
        }
    }

    private fun showDialog(dialog: Dialog) {
        dialog.setOnDismissListener {
            dialog.setOnDismissListener(null)
            nextShow()
        }
        dialog.show()
    }

    private fun showDialogFragment(
        dialogFragment: DialogFragment
    ) {
        dialogFragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onLifecycleCancel() {
                dialogFragment.lifecycle.removeObserver(this)
                nextShow()
            }
        })

        dialogFragment.show(fragmentManager, dialogFragment::class.java.simpleName)
    }

    private data class DialogElement(val dialog: Any, val action: () -> Boolean)

}