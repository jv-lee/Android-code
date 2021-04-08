package com.lee.proxy

import com.lee.proxy.dynamic.BookProxyHandler
import com.lee.proxy.static.BookProxy
import org.junit.Test
import java.lang.reflect.Proxy

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    /**
     * 静态代理实现
     */
    @Test
    fun staticProxyTest() {
        val book = BookImpl()
        val bookProxy = BookProxy(book)
        bookProxy.printBookName("《Android开发与艺术》")
    }

    /**
     * 动态代理实现
     */
    @Test
    fun dynamicProxyTest() {
        val book = BookImpl()
        val bookProxyHandler = BookProxyHandler(book)
        val iBook = Proxy.newProxyInstance(
            BookImpl::class.java.classLoader,
            BookImpl::class.java.interfaces,
            bookProxyHandler
        ) as IBook
        iBook.printBookName("《Android开发与艺术》")
    }
}