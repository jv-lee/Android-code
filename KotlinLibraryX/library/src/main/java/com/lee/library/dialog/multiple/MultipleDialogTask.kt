package com.lee.library.dialog.multiple

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

/**
 * @author jv.lee
 * @date 2021/8/23
 * @description 多Dialog展示任务工具类
 * 通过 addAction添加dialog展示任务 (后进先出原则)
 * 最终通过nextShow开始所有dialog任务顺序展示
 */
class MultipleDialogTask(fragmentManager: FragmentManager) {

    private val stack: Stack<DialogElement<*>> = Stack()
    private val multipleDialogAdapter = MultipleDialogAdapter(fragmentManager) { nextShow() }

    fun <T> addAction(dialog: T, action: () -> Boolean = { true }): MultipleDialogTask {
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
                    multipleDialogAdapter.switchShowType(dialog)
                } ?: kotlin.run {
                nextShow()
            }
        }.onFailure {
            nextShow()
        }

    }

    private data class DialogElement<T>(val dialog: T, val action: () -> Boolean)

}