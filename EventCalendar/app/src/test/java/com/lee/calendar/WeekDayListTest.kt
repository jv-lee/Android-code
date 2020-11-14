package com.lee.calendar

import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import org.junit.Test
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class WeekDayListTest {


    @Test
    fun text(){
        val today = CalendarUtils.getTodayNumber(2020, 9)
        val dayArray = arrayListOf<DayEntity>()
        val calendar = CalendarUtils.setWeekToSunday(2020, 9, 2)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        for (index in 0..6) {
            calendar.add(Calendar.DAY_OF_MONTH, if(index == 0) 0 else 1)
            println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DAY_OF_MONTH)}")
//            dayArray.add(
//                DayEntity(
//                    isSelected = index == 0,
//                    year = year,
//                    month = month,
//                    day = day,
//                    startIndex = index,
//                    isToDay = day == today
//                )
//            )
        }
    }
}