package com.lee.calendar

import com.lee.calendar.manager.CalendarManager
import com.lee.calendar.manager.CalendarManager2
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
class CalendarManager2Test {

    @Test
    fun test() {
        val cm = CalendarManager2(false)
        val initMonthData = cm.initDateList()
        println(initMonthData)

        val loadPrevDateList = cm.loadNextDateList()
        println(loadPrevDateList)

        val loadPrevDateList1 = cm.loadNextDateList()
        println(loadPrevDateList1)

//        val cm2 = CalendarManager2(false)
//        val initWeekData = cm2.initDateList()
//        println(initWeekData)
    }
}