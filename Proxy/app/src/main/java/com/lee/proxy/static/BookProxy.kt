package com.lee.proxy.static

import com.lee.proxy.IBook

/**
 * @author jv.lee
 * @date 4/8/21
 * @description 静态代理
 */
class BookProxy(
    private val iBook: IBook,
    private var startFun: () -> Unit = { println("run fun start.") },
    private var endFun: () -> Unit = { println("run fun end.") }
) : IBook {
    override fun printBookName(name: String) {
        //具体实现之前的操作
        startFun()
        iBook.printBookName(name)
        //具体实现之后的操作
        endFun()
    }

}