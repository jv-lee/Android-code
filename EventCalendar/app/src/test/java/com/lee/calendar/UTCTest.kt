package com.lee.calendar

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jv.lee
 * @date 2020/12/8
 * @description
 */
class UTCTest {

    @Test
    fun test1(){
        val timeZone = TimeZone.getDefault()
        println(timeZone.id)
    }

    @Test
    fun test() {
        val default = TimeZone.getDefault()

//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        val calendar2 = Calendar.getInstance()

        println(formatDate(calendar2.timeInMillis))

//        TimeZone.setDefault(default)
        val calendar = Calendar.getInstance()

        println(formatDate(calendar.timeInMillis))
    }

    fun formatDate(millis: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(millis)
    }
}