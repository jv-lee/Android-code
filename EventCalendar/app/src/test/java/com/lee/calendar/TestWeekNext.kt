package com.lee.calendar

import org.junit.Test
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class TestWeekNext {
    @Test
    fun test(){
//        val calendar = Calendar.getInstance()
//        calendar.set(2020,10,28)
//        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DATE)}")
//        calendar.add(Calendar.WEEK_OF_YEAR,1)
//        //将日期定位到当周第一天
//        CalendarUtils.setWeekOfOneDay(calendar)
//        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DATE)}")
        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.WEEK_OF_MONTH,1)
        calendar.set(Calendar.DAY_OF_WEEK,1)
        println(calendar.get(Calendar.DAY_OF_MONTH))
    }
}