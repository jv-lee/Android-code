package com.lee.review

import org.junit.Test

/**
 * @author jv.lee
 * @date 2021/4/22
 * @description
 */
class Test {

    @Test
    fun test() {
        val failDetail = arrayListOf<String>("1", "2", "3")
        val excludeLocalIds: List<Int> = failDetail.map {
            kotlin.runCatching {
                throw RuntimeException()
                it.toInt()
            }
                .getOrDefault(0)
        }.filter { it != 0 }
        println(excludeLocalIds)
    }

    data class A(val int: Int?= null)

    @Test
    fun test2() {
        val itemCount: Int? = 4
        itemCount?.takeIf { it > 5 }.run { println("run") }
    }
}