package com.lee.calendar

import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeekUnitTest {
    @Test
    fun addition_isCorrect() {
//        val calendar = Calendar.getInstance()
//        val week = calendar.get(Calendar.DAY_OF_WEEK)
//        calendar.set(Calendar.DATE, 1)
        //日期回滚一天，为当月最后一天
//        calendar.roll(Calendar.DATE, -1)
//        println(calendar.get(Calendar.DAY_OF_MONTH))
//        calendar.set(2020,9,22)

        //获取当天为开始的 当前一周数据
//        val weekDayList = CalendarUtils.getWeekDayList(2020, 10, 20)
//        print(weekDayList)
        var index = 0
        var calendar = getMoveWeekData(index)

        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DAY_OF_MONTH)}")

        calendar = getMoveWeekData(-(++index))
        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DAY_OF_MONTH)}")

        calendar = getMoveWeekData(-(++index))
        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DAY_OF_MONTH)}")

        calendar = getMoveWeekData(-(++index))
        println("${calendar.get(Calendar.YEAR)} - ${calendar.get(Calendar.MONTH)} - ${calendar.get(Calendar.DAY_OF_MONTH)}")
    }

    private fun getMoveWeekData(movePosition:Int):Calendar{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2020)
        calendar.set(Calendar.MONTH, 10)
        calendar.set(Calendar.DAY_OF_MONTH,5)
        calendar.add(Calendar.DAY_OF_MONTH, movePosition * 7)
        return calendar
    }

}