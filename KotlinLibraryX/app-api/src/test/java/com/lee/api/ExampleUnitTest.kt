package com.lee.api

import com.lee.api.ExtendsOrSuper.One
import com.lee.api.ExtendsOrSuper.Two
import org.junit.Assert.assertEquals
import org.junit.Test
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

    @Test
    fun test(){
        val one = One()
        val two = Two()
        println("One.one_1>>>>>>>${Two.one_1}")
        println("one.one_1>>>>>>>${One.one_1}")
    }
}