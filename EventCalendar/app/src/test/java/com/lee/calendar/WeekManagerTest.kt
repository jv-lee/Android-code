package com.lee.calendar

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.manager.CalendarManager
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/5
 * @description
 */
class WeekManagerTest {
    @Test
    fun test(){
        val calendarManager =
            CalendarManager(prevMonthCount = 12)
        val data: ArrayList<DateEntity> = calendarManager.getInitWeekData()

        println(data)
    }
}