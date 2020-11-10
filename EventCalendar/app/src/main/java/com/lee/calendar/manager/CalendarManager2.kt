package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/11/6
 * @description
 */
class CalendarManager2(
    private val startYear: Int,
    private val startMonth: Int,
    private val startDay: Int,
    private val loadMoreMonthCount:Int = 6
):ICalendarData {
    private var monthCalendar = Calendar.getInstance()

    private var weekCalendar = Calendar.getInstance()

    private var first = true

    override fun initMonthList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.set(startYear, startMonth, startDay)
        startCalendar.add(Calendar.MONTH, -1)

        val endCalendar = Calendar.getInstance()

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

            val today = CalendarUtils.isToday(year, month)
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
        endCalendar.add(Calendar.MONTH,1)

        val dateList = arrayListOf<DateEntity>()
        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH))
        ) {
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DATE)

            val today = CalendarUtils.isToday(year, month)
            val dayArray = arrayListOf<DayEntity>()
            val calendar = CalendarUtils.getFirstWeekDay(year, month, day)
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
            CalendarUtils.setWeekOfOneDay(startCalendar)
        }
        return dateList
    }

    /**
     * 向后填充月份数据
     */
    override fun loadMoreMonthList(): ArrayList<DateEntity> {
        val startCalendar = monthCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(monthCalendar.get(Calendar.YEAR),monthCalendar.get(Calendar.MONTH),monthCalendar.get(Calendar.DATE))
        endCalendar.add(Calendar.MONTH,loadMoreMonthCount)

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

            val today = CalendarUtils.isToday(year, month)
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
        monthCalendar = endCalendar
        return dateList
    }


    override fun loadMoreWeekList():ArrayList<DateEntity>{
        val startCalendar = weekCalendar
        if (first) {
            first =false
            startCalendar.add(Calendar.MONTH,1)
        }

        val endCalendar = Calendar.getInstance()
        endCalendar.set(weekCalendar.get(Calendar.YEAR),weekCalendar.get(Calendar.MONTH),weekCalendar.get(Calendar.DATE))
        endCalendar.add(Calendar.MONTH,loadMoreMonthCount)


        val dateList = arrayListOf<DateEntity>()
        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH))
        ) {
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DATE)

            val today = CalendarUtils.isToday(year, month)
            val dayArray = arrayListOf<DayEntity>()
            val calendar = CalendarUtils.getFirstWeekDay(year, month, day)
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
            CalendarUtils.setWeekOfOneDay(startCalendar)
        }
        weekCalendar = endCalendar
        return dateList
    }

}