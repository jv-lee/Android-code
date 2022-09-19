package com.lee.library.tools

/**
 * 事件防抖帮助类 用户点击事件、执行事件
 * @author jv.lee
 * @date 2022/9/19
 */
class ShakeHelper {
    companion object {
        private const val TIME_SPAN: Long = 1000
        private var lastTime: Long = 0

        fun run(timeSpan: Long = TIME_SPAN, block: () -> Unit) {
            val intervalTime = System.currentTimeMillis() - lastTime
            if (intervalTime < timeSpan) return
            lastTime = System.currentTimeMillis()
            block()
        }
    }
}