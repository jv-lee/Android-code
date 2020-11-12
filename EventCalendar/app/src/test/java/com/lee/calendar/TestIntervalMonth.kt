package com.lee.calendar

import com.lee.calendar.utils.CalendarUtils
import org.junit.Test
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description
 */
class TestIntervalMonth {

    @Test
    fun test(){
        val startCalendar = Calendar.getInstance()
        startCalendar.set(2019,5,13)
        val endCalendar = Calendar.getInstance()
        endCalendar.set(2020,10,1)
        val count = CalendarUtils.getDiffMonthCount(startCalendar,endCalendar)
        print(count)
    }
}