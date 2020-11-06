package com.lee.calendar

import com.lee.calendar.manager.CalendarManager
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val calendarManager = CalendarManager()
        val monthList = calendarManager.getInitMonthData()

        monthList.addAll(0,calendarManager.getPrevMonthData())
        monthList.addAll(0,calendarManager.getPrevMonthData())
        monthList.addAll(0,calendarManager.getPrevMonthData())
        monthList.addAll(0,calendarManager.getPrevMonthData())
        monthList.addAll(0,calendarManager.getPrevMonthData())
        for (monthEntity in monthList) {
            println(monthEntity.toString())
        }

//        monthList.addAll(calendarManager.getNextMonthData())
//        monthList.addAll(calendarManager.getNextMonthData())
//        monthList.addAll(calendarManager.getNextMonthData())
//        monthList.addAll(calendarManager.getNextMonthData())
//        monthList.addAll(calendarManager.getNextMonthData())
//        for (monthEntity in monthList) {
//            println(monthEntity.toString())
//        }

//        println("------------------------添加上下页数据后--------------------------------")
//
//        val prevMonthData = calendarManager.getPrevMonthData()
//        val nextMonthData = calendarManager.getNextMonthData()
//        monthList.addAll(0, prevMonthData)
//        monthList.addAll(nextMonthData)
//        for (monthEntity in monthList) {
//            println(monthEntity.toString())
//        }
//
//        println("------------------------添加上下页数据后--------------------------------")
//
//        val prevMonthData2 = calendarManager.getPrevMonthData()
//        val nextMonthData2 = calendarManager.getNextMonthData()
//        monthList.addAll(0, prevMonthData2)
//        monthList.addAll(nextMonthData2)
//        for (monthEntity in monthList) {
//            println(monthEntity.toString())
//        }
    }
}