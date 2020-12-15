package com.lee.calendar.widget.calendar.utils

import com.lee.calendar.widget.calendar.entity.DayEntity
import java.util.*

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
     * 计算两个时间内相隔月数
     * @param targetCalendar 目标时间
     * @param currentCalendar 当前时间
     * @return 相隔月数  负数向前？月  正数向后？月
     */
    fun getDiffMonthCount(targetCalendar: Calendar, currentCalendar:Calendar): Int {
        val num = targetCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)
        val yearNum = (targetCalendar.get(Calendar.YEAR) - currentCalendar.get(Calendar.YEAR)) * 12
        return num + yearNum
    }

    /**
     * 获取两个时间 长间距相隔多少个周
     * 优化方法
     */
    fun getDiffWeekCount(tagCalendar: Calendar, currentCalendar: Calendar): Int {
        val tag = setFirstDayOfWeek(tagCalendar.also { it.time })

        val current = setFirstDayOfWeek(currentCalendar.also { it.time })
        val diff = tag.timeInMillis - current.timeInMillis
        val nd = 1000 * 24 * 60 * 60.toLong()
        val day = diff / nd
        val week = day / 7
        println(week.toInt())
        return week.toInt()
    }

    /**
     * 设置当前时间为当月第一天
     */
    fun setFirstDayOfMonth(calendar: Calendar): Calendar {
        calendar.set(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    /**
     * 将日历设置为当周的第一天 (周日)
     */
    fun setFirstDayOfWeek(calendar: Calendar):Calendar{
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    /**
     * 将当前日期设置为这周第一天 (周日)
     * @return 日历对象
     */
    fun setFirstDayOfWeek(year: Int, month: Int, day: Int): Calendar {
        return Calendar.getInstance().also {
            it.set(year,month,day)
            it.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - it.get(Calendar.DAY_OF_WEEK))
            it.set(Calendar.HOUR_OF_DAY, 0);
            it.set(Calendar.MINUTE, 0);
            it.set(Calendar.SECOND, 0);
            it.set(Calendar.MILLISECOND, 0);
        }
    }

    /**
     * 将当月第一周这职位这周第一天（周日）
     */
    fun setFirstWeekToSunday(year: Int, month: Int, day: Int):Calendar {
        return Calendar.getInstance().also {
            it.set(year,month,day)
            it.set(Calendar.WEEK_OF_MONTH,7)
            it.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
        }
    }

    /**
     * 获取当月最大天数
     * @param year 年 1995
     * @param month 0-11
     */
    fun getMaxDayOfMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        //日期设置为当月第一天
        calendar.set(year,month,1)
        //日期回滚一天，为当月最后一天
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     * 获取当年最大周数
     * @param year 年 1995
     */
    fun getMaxWeekOfYear(year:Int):Int{
        val calendar = Calendar.getInstance()
        calendar.set(year + 1, 0, 1)
        calendar.add(Calendar.DAY_OF_YEAR,-1)
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        if (weekNumber == 1) {
            calendar.add(Calendar.WEEK_OF_YEAR,-1)
        }
        return calendar.get(Calendar.WEEK_OF_YEAR)
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
     * 当月第一天是否为周日
     */
    fun firstDayIsSunday(year:Int, month: Int): Boolean {
        val calendar = Calendar.getInstance().also {
            it.set(year,month,1)
        }
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    /**
     * 最后一周是否为当前月份
     */
    fun lastWeekDayIsToMonth(year: Int,month: Int):Boolean{
        val lastDayIsWeek =
            getMaxWeekOfMonth(
                year,
                month
            )
        val calendar = Calendar.getInstance().also {
            it.set(year,month,1)
            it.set(Calendar.WEEK_OF_MONTH,lastDayIsWeek)
            it.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY)
        }
        return calendar.get(Calendar.MONTH) == month
    }

    /**
     * 获取当前月总周数
     */
    fun getMaxWeekOfMonth(year: Int, month: Int):Int {
        val day =
            getMaxDayOfMonth(
                year,
                month
            )
        val calendar = Calendar.getInstance().also {
            it.set(year,month,day)
        }
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }

    /**
     * 根据年月获取当天日期， 非当年当月返回 -1
     */
    fun getTodayNumber(year: Int, month: Int): Int {
        val isToMonth = Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance()
            .get(Calendar.MONTH) == month
        return if (isToMonth) Calendar.getInstance().get(Calendar.DAY_OF_MONTH) else -1
    }

    /**
     * 根据年月日 获取当7天的数据
     */
    fun getWeekDayList(year: Int, month: Int, day: Int): ArrayList<DayEntity> {
        val today =
            getTodayNumber(
                year,
                month
            )
        val dayArray = arrayListOf<DayEntity>()
        val calendar =
            setFirstDayOfWeek(
                year,
                month,
                day
            )
        for (index in 0..6) {
            calendar.add(Calendar.DAY_OF_MONTH, if(index == 0) 0 else 1)
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
        return dayArray
    }

    /**
     * 根据年月 获取当月所有天数据
     */
    fun getMonthDayList(year: Int, month: Int, startIndex: Int): ArrayList<DayEntity> {
        val today =
            getTodayNumber(
                year,
                month
            )
        val dayArray = arrayListOf<DayEntity>()
        val dayCount =
            getMaxDayOfMonth(
                year,
                month
            )
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
        return getAttachDayList(
            dayArray,
            year,
            month,
            startIndex
        )
    }

    /**
     * 获取默认填充数据， 补充一月中的月头 和 月尾数据
     */
    fun getAttachDayList(
        data: ArrayList<DayEntity>,
        year: Int,
        month: Int,
        startIndex: Int
    ): ArrayList<DayEntity> {
        getAttachPrevDay(
            data,
            year,
            month,
            startIndex
        )
        getAttachNextDay(
            data,
            year,
            month,
            startIndex
        )
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

        for (index in 0 until count) {
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