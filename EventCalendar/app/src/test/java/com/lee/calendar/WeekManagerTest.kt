package com.lee.calendar

import com.lee.calendar.entity.DataEntity
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/5
 * @description
 */
class WeekManagerTest {
    @Test
    fun test(){
        val calendarManager = CalendarManager(prevMonthCount = 12)
        val data: ArrayList<DataEntity> = calendarManager.getInitWeekData()

        println(data)
    }
}