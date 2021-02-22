package com.lee.ui

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(math(-30))
        println(math(30))
    }

    private fun math(number: Int): Int {
        return when {
            number > 0 -> {
                return -number
            }
            number < 0 -> {
                return abs(number)
            }
            else -> {
                number
            }
        }
    }
}