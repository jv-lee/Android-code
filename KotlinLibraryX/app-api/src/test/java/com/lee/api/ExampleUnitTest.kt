package com.lee.api

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val time = System.currentTimeMillis()
        println(SimpleDateFormat("yyyy-MM-dd").format(Date(time)))
        val time2 = time / 1000
        val time3 = time2 * 1000
        println(SimpleDateFormat("yyyy-MM-dd").format(Date(time3)))
        println(SimpleDateFormat("yyyy-MM-dd").format(Date("100000000".toLong())))
    }
}