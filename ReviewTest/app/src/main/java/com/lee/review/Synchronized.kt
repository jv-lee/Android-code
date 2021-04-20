package com.lee.review

import kotlin.jvm.Synchronized

/**
 * @author jv.lee
 * @date 2021/4/20
 * @description
 */
class Synchronized {

    var count = 0

    /**
     * 同步方法
     */
    @Synchronized
    fun fun1(): Int {
        ++count
        return count
    }

    /**
     * 同步代码块
     */
    fun fun2(): Int {
        synchronized(this) {
            ++count
        }
        return count
    }

    fun fun3(): Int {
        return ++count
    }

}