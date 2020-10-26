package com.lee.calendar

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val monthList = CalendarManager.getInstance().getInitMonthData()
        for (monthEntity in monthList) {
            println(monthEntity.toString())
        }

        println("------------------------添加上下页数据后--------------------------------")

        val prevMonthData = CalendarManager.getInstance().getPrevMonthData()
        val nextMonthData = CalendarManager.getInstance().getNextMonthData()
        monthList.addAll(0, prevMonthData)
        monthList.addAll(nextMonthData)
        for (monthEntity in monthList) {
            println(monthEntity.toString())
        }

        println("------------------------添加上下页数据后--------------------------------")

        val prevMonthData2 = CalendarManager.getInstance().getPrevMonthData()
        val nextMonthData2 = CalendarManager.getInstance().getNextMonthData()
        monthList.addAll(0, prevMonthData2)
        monthList.addAll(nextMonthData2)
        for (monthEntity in monthList) {
            println(monthEntity.toString())
        }
    }
}