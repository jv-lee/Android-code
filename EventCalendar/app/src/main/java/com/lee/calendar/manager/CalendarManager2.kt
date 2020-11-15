package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
class CalendarManager2(
    private val isMonth: Boolean,
    private val initPrevMonthCount:Int = 1,
    private val initNextMonthCount:Int = 1,
    private val loadPNMonthCount: Int = 1    //向前分页/向后分页count
) : ICalendarData2 {

    private var startIndexCalendar =
        Calendar.getInstance().also { it.add(Calendar.MONTH, -(initPrevMonthCount)) }
    private var endIndexCalendar =
        Calendar.getInstance().also { it.add(Calendar.MONTH, initNextMonthCount) }

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
        startCalendar.add(Calendar.MONTH, -initPrevMonthCount)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH, initNextMonthCount)

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

    private fun loadPrevMonthList(): ArrayList<DateEntity> {
        val startCalendar = startIndexCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DATE)
        )
        endCalendar.add(Calendar.MONTH, -loadPNMonthCount)

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

            dateList.add(
                0,
                DateEntity(
                    year, month,
                    CalendarUtils.getAttachDayList(dayArray, year, month, startIndex), startIndex
                )
            )
        }
        startIndexCalendar = startCalendar
        return dateList
    }

    private fun loadNextMonthList(): ArrayList<DateEntity> {
        val startCalendar = endIndexCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DATE)
        )
        endCalendar.add(Calendar.MONTH, loadPNMonthCount)

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
        endIndexCalendar = startCalendar
        return dateList
    }

    private fun initWeekList(): ArrayList<DateEntity> {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.MONTH, -initPrevMonthCount)
        startCalendar.set(Calendar.DATE, 1)

        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.MONTH, initNextMonthCount)
        endCalendar.set(Calendar.DATE, 1)
        endCalendar.roll(Calendar.DATE, -1)

        val dateList = arrayListOf<DateEntity>()
        while (true) {
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

            if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH) &&
                startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)
            ) break

            startCalendar.add(Calendar.WEEK_OF_YEAR, 1)
            //将日期定位到当周第一天
            CalendarUtils.setWeekToSunday(startCalendar)
        }
        return dateList
    }

    private fun loadPrevWeekList(): ArrayList<DateEntity> {
        val startCalendar = startIndexCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DATE)
        )
        endCalendar.add(Calendar.MONTH, -loadPNMonthCount)

        val dateList = arrayListOf<DateEntity>()

        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
                    )
        ) {
            startCalendar.add(Calendar.MONTH, -1)


            var weekCount = CalendarUtils.getMonthWeekCount(
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH)
            )
            if (!CalendarUtils.lastWeekDayIsToMonth(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH)
                )
            ) {
                weekCount--
            }
            val calendar = Calendar.getInstance().also {
                it.set(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DATE)
                )
            }
            for (weekIndex in 1..weekCount) {
                calendar.set(Calendar.WEEK_OF_MONTH, weekIndex)
                calendar.set(Calendar.DAY_OF_WEEK, 1)
                val today = CalendarUtils.getTodayNumber(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH)
                )
                val dayArray = arrayListOf<DayEntity>()
                for (index in Calendar.SUNDAY..Calendar.SATURDAY) {
                    dayArray.add(
                        DayEntity(
                            isSelected = index == 1,
                            year = calendar.get(Calendar.YEAR),
                            month = calendar.get(Calendar.MONTH),
                            day = calendar.get(Calendar.DAY_OF_MONTH),
                            startIndex = -1,
                            isToDay = today == calendar.get(Calendar.DAY_OF_MONTH)
                        )
                    )
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                dateList.add(
                    DateEntity(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        dayArray,
                        -1
                    )
                )
            }
        }
        startIndexCalendar = startCalendar
        return dateList
    }

    private fun loadNextWeekList(): ArrayList<DateEntity> {
        val startCalendar = endIndexCalendar

        val endCalendar = Calendar.getInstance()
        endCalendar.set(
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DATE)
        )
        endCalendar.add(Calendar.MONTH, loadPNMonthCount)

        val dateList = arrayListOf<DateEntity>()

        while (!(startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                    startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
                    )
        ) {
            startCalendar.add(Calendar.MONTH, 1)

            val weekCount = CalendarUtils.getMonthWeekCount(
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH)
            )
            val startWeek = if (CalendarUtils.firstDayIsSunday(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH)
                )
            ) 1 else 2

            val calendar = Calendar.getInstance().also {
                it.set(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DATE)
                )
            }
            for (weekIndex in startWeek..weekCount) {
                calendar.set(Calendar.WEEK_OF_MONTH, weekIndex)
                calendar.set(Calendar.DAY_OF_WEEK, 1)
                val today = CalendarUtils.getTodayNumber(
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH)
                )
                val dayArray = arrayListOf<DayEntity>()
                for (index in Calendar.SUNDAY..Calendar.SATURDAY) {
                    dayArray.add(
                        DayEntity(
                            isSelected = index == 1,
                            year = calendar.get(Calendar.YEAR),
                            month = calendar.get(Calendar.MONTH),
                            day = calendar.get(Calendar.DAY_OF_MONTH),
                            startIndex = -1,
                            isToDay = today == calendar.get(Calendar.DAY_OF_MONTH)
                        )
                    )
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                dateList.add(
                    DateEntity(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        dayArray,
                        -1
                    )
                )
            }
        }
        endIndexCalendar = startCalendar
        return dateList
    }

}