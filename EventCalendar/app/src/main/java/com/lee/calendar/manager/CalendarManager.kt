package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class CalendarManager(
    private val startYear: Int,
    private val startMonth: Int,
    private val startDay: Int,
    private val endMonth:Int = 6
): ICalendarData {

    override fun initMonthList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.set(startYear, startMonth, startDay)
        startCalendar.add(Calendar.MONTH, -1)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH,endMonth)

        val dateList = arrayListOf<DateEntity>()
        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH))
        ) {
            startCalendar.add(Calendar.MONTH, 1)
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)

            //获取当月1号遍历数量
            startCalendar.set(Calendar.DATE, 1)
            val startIndex = startCalendar.get(Calendar.DAY_OF_WEEK) - 1

            val today = CalendarUtils.getTodayNumber(year, month)
            val dayArray = arrayListOf<DayEntity>()
            val dayCount = CalendarUtils.getMaxDayCountByMonth(year, month)
            for (index in 1..dayCount) {
                dayArray.add(
                    DayEntity(
                        isSelected = index == 1,
                        year = year,
                        month = month,
                        day = index,
                        startIndex = startIndex,
                        isToDay = today == index
                    )
                )
            }

            dateList.add(
                DateEntity(
                    year, month,
                    CalendarUtils.getAttachDayList(dayArray, year, month, startIndex), startIndex
                )
            )
        }
        return dateList
    }

    override fun initWeekList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.set(startYear, startMonth, startDay)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH,endMonth)
        endCalendar.add(Calendar.MONTH,1)

        val dateList = arrayListOf<DateEntity>()
        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH))
        ) {
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DATE)

            val today = CalendarUtils.getTodayNumber(year, month)
            val dayArray = arrayListOf<DayEntity>()
            val calendar = CalendarUtils.setWeekToSunday(year, month, day)
            for (index in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, if (index == 0) 0 else 1)
                dayArray.add(
                    DayEntity(
                        isSelected = index == 0,
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        day = calendar.get(Calendar.DAY_OF_MONTH),
                        startIndex = index,
                        isToDay = calendar.get(Calendar.DAY_OF_MONTH) == today
                    )
                )
            }

            dateList.add(DateEntity(year, month, dayArray, -1))
            startCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            //将日期定位到当周第一天
            CalendarUtils.setWeekToSunday(startCalendar)
        }
        return dateList
    }

}