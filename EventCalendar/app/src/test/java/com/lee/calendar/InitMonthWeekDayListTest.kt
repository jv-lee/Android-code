package com.lee.calendar

import com.lee.calendar.manager.CalendarManager
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

    @Test
    fun testNextMonthList(){
        val calendarManager = CalendarManager(2018, 0, 1)
        val dateList1 = calendarManager.loadMoreMonthList()
        println(dateList1)
        println("下一页")
        val dateList2 = calendarManager.loadMoreMonthList()
        println(dateList2)
        println("下一页")
        val dateList3 = calendarManager.loadMoreMonthList()
        println(dateList3)
    }

    @Test
    fun testNextWeekList(){
        val calendarManager = CalendarManager(2018, 0, 1)
        val dateList1 = calendarManager.loadMoreWeekList()
        println(dateList1)
        println("下一页")
        val dateList2 = calendarManager.loadMoreWeekList()
        println(dateList2)
        println("下一页")
        val dateList3 = calendarManager.loadMoreWeekList()
        println(dateList3)
    }
}