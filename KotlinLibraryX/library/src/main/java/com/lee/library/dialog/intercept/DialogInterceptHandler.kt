package com.lee.library.dialog.intercept

/**
 * @author jv.lee
 * @data 2021/8/26
 * @description
 */
class DialogInterceptHandler<T> {
    var firstIntercept: DialogIntercept<T>? = null

    fun add(interceptChain: DialogIntercept<T>) {
        if (firstIntercept == null) {
            firstIntercept = interceptChain
            return
        }

        var node = firstIntercept
        while (true) {
            if (node?.next == null) {
                node?.next = interceptChain
                break
            }
            node = node.next
        }
    }

    fun intercept(item: T) {
        firstIntercept?.intercept(item)
    }

}