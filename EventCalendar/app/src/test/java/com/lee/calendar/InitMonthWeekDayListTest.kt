package com.lee.calendar

import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class InitMonthWeekDayListTest {

    @Test
    fun testInitMonthList() {
        val calendarManager = CalendarManager(2020, 0, 1)
        val dateList = calendarManager.initMonthList()
        print(dateList)
    }

    @Test
    fun testInitWeekList() {
        val calendarManager = CalendarManager(2020, 0, 1)
        val dateList = calendarManager.initWeekList()
        print(dateList)
    }

}