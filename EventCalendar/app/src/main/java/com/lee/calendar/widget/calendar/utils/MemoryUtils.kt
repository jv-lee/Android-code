package com.lee.calendar.widget.calendar.utils

/**
 * @author jv.lee
 * @date 2021/1/27
 * @description
 */
object MemoryUtils {

    const val M = 1024 * 1024

    fun hasMemory(): Boolean {
        return getAvailableMemory() > 24
    }

    //获取当前最大内存
    fun getMaxMemory(): Long {
        return toM(Runtime.getRuntime().maxMemory())
    }

    //获取已用内存
    fun getUsedMemory(): Long {
        return toM(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
    }

    //获取可用内存
    fun getAvailableMemory(): Long {
        return toM(Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))
    }

    private fun toM(memory: Long): Long {
        return memory / M
    }
}