package com.lee.calendar.utils

import com.lee.calendar.entity.DayEntity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
object CalendarUtils {

    /**
     * 转换月数据 0 - 11 的月常量转换为 字符串显示 1 - 12
     */
    fun getMonthNumber(month: Int): String {
        return (month + 1).toString()
    }

    /**
     * 获取当月总天数
     */
    fun getCurrentMonthMaxDay(calendar: Calendar = Calendar.getInstance()): Int {
        //日期设置为当月第一天
        calendar.set(Calendar.DATE, 1)
        //日期回滚一天，为当月最后一天
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     * 获取指定年月 当月总天数
     * @param year 年 1995
     * @param month 0-11
     */
    fun getMonthMaxDay(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        return getCurrentMonthMaxDay(calendar)
    }

    /**
     * 获取当前时间为星期几
     */
    fun getDayOfWeek(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * 将日历设置为当周的第一天
     * @return 日历对象
     */
    fun getFirstWeekDay(year: Int, month: Int, day: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK))
        return calendar
    }

    /**
     * 根据年月获取当天日期， 非当年当月返回 -1
     */
    fun isToday(year: Int, month: Int): Int {
        val isToMonth = Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance()
            .get(Calendar.MONTH) == month
        return if (isToMonth) Calendar.getInstance().get(Calendar.DAY_OF_MONTH) else -1
    }

    /**
     * 根据年月日 获取当7天的数据
     */
    fun getWeekDayList(year: Int, month: Int, day: Int): ArrayList<DayEntity> {
        val today = isToday(year, month)
        val dayArray = arrayListOf<DayEntity>()
        val calendar = getFirstWeekDay(year, month, day)
        val weekFirstDay = calendar.get(Calendar.DAY_OF_MONTH)
        val monthMaxDay = getMonthMaxDay(year, month)
        var nextMonthDay = 0
        for (index in 0..6) {
            var day = weekFirstDay + index
            day = if(day <= monthMaxDay) day else ++nextMonthDay
            dayArray.add(
                DayEntity(
                    isSelected = index == 0,
                    year = year,
                    month = month,
                    day = day,
                    startIndex = index,
                    isToDay = day == today
                )
            )
        }
        return dayArray
    }

    /**
     * 根据年月 获取当月所有天数据
     */
    fun getMonthDayList(year: Int, month: Int, startIndex: Int): ArrayList<DayEntity> {
        val today = isToday(year, month)
        val dayArray = arrayListOf<DayEntity>()
        val dayCount = getMonthMaxDay(year, month)
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
        return getAttachDayList(dayArray, year, month, startIndex)
    }

    /**
     * 获取默认填充数据， 补充一月中的月头 和 月尾数据
     */
    private fun getAttachDayList(
        data: ArrayList<DayEntity>,
        year: Int,
        month: Int,
        startIndex: Int
    ): ArrayList<DayEntity> {
        getAttachPrevDay(data, year, month, startIndex)
        getAttachNextDay(data, year, month, startIndex)
        return data
    }

    private fun getAttachPrevDay(
        data: ArrayList<DayEntity>,
        year: Int,
        month: Int,
        startIndex: Int
    ): ArrayList<DayEntity> {
        val calendar = Calendar.getInstance()
        //设置时间为当月1号
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, 1)

        //获取当月1号遍历数量
        val count = calendar.get(Calendar.DAY_OF_WEEK) - 2

        //向上调整一个月的时间
        if (month == 0) {
            calendar.set(Calendar.YEAR, year - 1)
            calendar.set(Calendar.MONTH, 11)
        } else {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
        }

        //设置为最后一天
        calendar.roll(Calendar.DATE, -1)

        for (index in 0..count) {
            data.add(
                0,
                DayEntity(
                    isToMonth = false,
                    year = year,
                    month = calendar.get(Calendar.MONTH),
                    day = calendar.get(Calendar.DAY_OF_MONTH) - index,
                    startIndex = startIndex
                )
            )
        }
        return data
    }

    private fun getAttachNextDay(
        data: ArrayList<DayEntity>,
        year: Int,
        month: Int,
        startIndex: Int,
        isFull: Boolean = true
    ): ArrayList<DayEntity> {
        val calendar = Calendar.getInstance()
        //设置时间为当月最后一天
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)

        val count = if (isFull) 42 - data.size else 6 - calendar.get(Calendar.DAY_OF_WEEK)

        if (month == 11) {
            calendar.set(Calendar.YEAR, year + 1)
            calendar.set(Calendar.MONTH, 0)
        } else {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month + 1)
        }

        calendar.set(Calendar.DATE, 1)

        for (index in 0..count) {
            data.add(
                DayEntity(
                    isToMonth = false,
                    year = year,
                    month = calendar.get(Calendar.MONTH),
                    day = calendar.get(Calendar.DAY_OF_MONTH) + index,
                    startIndex = startIndex
                )
            )
        }
        return data
    }

}