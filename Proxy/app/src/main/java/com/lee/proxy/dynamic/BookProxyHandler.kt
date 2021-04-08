package com.lee.proxy.dynamic

import com.lee.proxy.IBook
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author jv.lee
 * @date 4/8/21
 * @description
 */
class BookProxyHandler(
    private val iBook: IBook,
    private var startFun: () -> Unit = { println("run fun start.") },
    private var endFun: () -> Unit = { println("run fun end.") }
) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        startFun()
        val invoke = method?.invoke(iBook, *(args ?: emptyArray()))
        endFun()
        return invoke
    }
}