package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
class CalendarManager2(private val isMonth: Boolean) : ICalendarData2 {

    //初始化向前向后月份数量
    private val initPNCount = 1
    //向前分页/向后分页count
    private val loadCount = 3

    private var prevCalendar = Calendar.getInstance().also { it.add(Calendar.MONTH,-(initPNCount)) }
    private var nextCalendar = Calendar.getInstance().also { it.add(Calendar.MONTH,initPNCount) }

    override fun initDateList(): ArrayList<DateEntity> {
        return if (isMonth) initMonthList() else initWeekList()
    }

    override fun loadNextDateList(): ArrayList<DateEntity> {
        return if (isMonth) loadNextMonthList() else loadNextWeekList()
    }

    override fun loadPrevDateList(): ArrayList<DateEntity> {
        return if (isMonth) loadPrevMonthList() else loadPrevWeekList()
    }

    private fun initMonthList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.MONTH, -initPNCount)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH, initPNCount)

        val dateList = arrayListOf<DateEntity>()

        while (true) {
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

            if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
            ) break
            startCalendar.add(Calendar.MONTH, 1)
        }
        return dateList
    }

    private fun initWeekList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.MONTH, -initPNCount)
        startCalendar.set(Calendar.DATE, 1)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH, initPNCount)
        endCalendar.set(Calendar.DATE, 1)
        endCalendar.roll(Calendar.DATE, -1)

        val dateList = arrayListOf<DateEntity>()
        while (true) {
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DATE)

            val today = CalendarUtils.getTodayNumber(year, month)
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

            if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH) &&
                startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)
            ) break

            startCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            //将日期定位到当周第一天
            CalendarUtils.setWeekOfOneDay(startCalendar)
        }
        return dateList
    }

    private fun loadPrevMonthList(): ArrayList<DateEntity> {
        val startCalendar = prevCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DATE))
        endCalendar.add(Calendar.MONTH, -loadCount)

        val dateList = arrayListOf<DateEntity>()

        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
                    )
        ) {
            startCalendar.add(Calendar.MONTH, -1)

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

            dateList.add(0,
                DateEntity(
                    year, month,
                    CalendarUtils.getAttachDayList(dayArray, year, month, startIndex), startIndex
                )
            )
        }
        prevCalendar = startCalendar
        return dateList
    }

    private fun loadPrevWeekList(): ArrayList<DateEntity> {
        return arrayListOf()
    }

    private fun loadNextMonthList(): ArrayList<DateEntity> {
        val startCalendar = nextCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH),startCalendar.get(Calendar.DATE))
        endCalendar.add(Calendar.MONTH, loadCount)

        val dateList = arrayListOf<DateEntity>()

        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
                    )
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
        nextCalendar = startCalendar
        return dateList
    }

    private fun loadNextWeekList(): ArrayList<DateEntity> {
        val startCalendar = nextCalendar
        startCalendar.add(Calendar.MONTH,1)

        val endCalendar = Calendar.getInstance()
        endCalendar.set(nextCalendar.get(Calendar.YEAR),nextCalendar.get(Calendar.MONTH),nextCalendar.get(Calendar.DATE))
        endCalendar.add(Calendar.MONTH,loadCount)
        endCalendar.set(Calendar.DATE, 1)
        endCalendar.roll(Calendar.DATE, -1)

        val dateList = arrayListOf<DateEntity>()
        while (true ) {
            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DATE)

            val today = CalendarUtils.getTodayNumber(year, month)
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

            if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH) &&
                startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)
            ) break

            startCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            CalendarUtils.setWeekOfOneDay(startCalendar)
        }
        nextCalendar = endCalendar
        return dateList
    }

}