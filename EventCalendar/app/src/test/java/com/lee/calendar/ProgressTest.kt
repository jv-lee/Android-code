package com.lee.calendar

import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/17
 * @description
 */
class ProgressTest {
    @Test
    fun progressTest(){
        val tagDay = 30f
        val comDay = 29f

        println((comDay) / (tagDay) * 100)

        val tagWord = 40000F
        val comWord = 60000F

        println((comWord) / (tagWord) * 100)
    }
}