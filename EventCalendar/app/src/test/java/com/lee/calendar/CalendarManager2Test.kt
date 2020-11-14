package com.lee.calendar

import com.lee.calendar.manager.CalendarManager2
import com.lee.calendar.utils.CalendarUtils
import org.junit.Test
import java.util.*

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

    @Test
    fun test2(){
        val firstWeekDay1 = CalendarUtils.setWeekToSunday(2020, 10, 14)
        println("${firstWeekDay1.get(Calendar.MONTH)} - ${firstWeekDay1.get(Calendar.DAY_OF_MONTH)}")
    }


    @Test
    fun test3(){
        val weekToSunday = setWeekToSunday(2020, 10, 14)
        println("${weekToSunday.get(Calendar.MONTH)} - ${weekToSunday.get(Calendar.DAY_OF_MONTH)}")
    }

    fun setWeekToSunday(year: Int, month: Int, day: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK))
//        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        return calendar
//        return CalendarUtils.setWeekToSunday(Calendar.getInstance().also {
//            it.set(year, month, day)
//        })
    }




}