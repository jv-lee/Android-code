package com.lee.calendar

import com.lee.calendar.widget.calendar.utils.CalendarUtils
import org.junit.Test

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class MaxWeekTest {

    @Test
    fun test(){


        println(CalendarUtils.getMaxDayCountByMonth(2020,11))
        println(CalendarUtils.getMaxWeekCountByYear(2023))


    }
}