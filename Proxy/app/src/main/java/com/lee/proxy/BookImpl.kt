package com.lee.proxy

import com.lee.proxy.IBook

/**
 * @author jv.lee
 * @date 4/8/21
 * @description
 */
class BookImpl : IBook {
    override fun printBookName(name: String) {
        println("bookName:$name")
    }
}