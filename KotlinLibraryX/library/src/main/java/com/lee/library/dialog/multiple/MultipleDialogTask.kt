package com.lee.library.dialog.multiple

import androidx.fragment.app.FragmentManager
import java.util.*

/**
 * 多Dialog展示任务工具类
 * 通过 addAction添加dialog展示任务 (后进先出原则)
 * 最终通过nextShow开始所有dialog任务顺序展示
 * @author jv.lee
 * @date 2021/8/23
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
            stack.pop()?.takeIf {
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