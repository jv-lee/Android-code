package com.lee.review

import java.util.*
import java.util.concurrent.ConcurrentSkipListSet

/**
 * @author jv.lee
 * @date 2021/4/20
 * @description 线程安全操作List
 */
class ThreadCollections {

    /**
     * 使用Collections.synchronizedList 转换同步List
     * 内部实现 所有读写操作都通过 synchronized （同步代码块包裹）
     */
    fun function1() {
        val list = Collections.synchronizedList<String>(arrayListOf())
        list.add("")
        list[0]
        list.remove("")
    }

    /**
     * 使用 synchronized（同步代码块） 包括List操作
     */
    fun function2() {
        val list = arrayListOf<String>()
        synchronized(this) {
            list.add("")
            list[0]
            list.remove("")
        }
    }

    /**
     * 使用 concurrent包下的数据结构操作
     */
    fun function3() {
        val set = ConcurrentSkipListSet<String>()
        set.add("")
        set.remove("")
        set.forEach {

        }
    }
}