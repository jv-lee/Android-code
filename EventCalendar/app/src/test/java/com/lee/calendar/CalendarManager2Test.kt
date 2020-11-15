package com.lee.calendar

import com.lee.calendar.manager.CalendarManager2
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
class CalendarManager2Test {

    @Test
    fun testWeekDate() {
        val cm = CalendarManager2(true,24,6)
        val dateList = cm.initDateList()

        val prev1 = cm.loadPrevDateList()

        val prev2 = cm.loadPrevDateList()

        val prev3 = cm.loadPrevDateList()

        val next1 = cm.loadNextDateList()

        val next2 = cm.loadNextDateList()

        val next3 = cm.loadNextDateList()

        dateList.addAll(0,prev1)
        dateList.addAll(0,prev2)
        dateList.addAll(0,prev3)

        dateList.addAll(next1)
        dateList.addAll(next2)
        dateList.addAll(next3)

        println(dateList)
    }

    @Test
    fun testMonthDate() {
        val cm = CalendarManager2(true)
        val dateList = cm.initDateList()

        val prev1 = cm.loadPrevDateList()

        val prev2 = cm.loadPrevDateList()

        val prev3 = cm.loadPrevDateList()

        val next1 = cm.loadNextDateList()

        val next2 = cm.loadNextDateList()

        val next3 = cm.loadNextDateList()

        dateList.addAll(0,prev1)
        dateList.addAll(0,prev2)
        dateList.addAll(0,prev3)

        dateList.addAll(next1)
        dateList.addAll(next2)
        dateList.addAll(next3)

        println(dateList)
    }

}