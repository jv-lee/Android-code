package com.lee.calendar.widget.calendar.utils

import android.annotation.SuppressLint
import com.lee.calendar.widget.calendar.entity.DayEntity
import java.text.SimpleDateFormat
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
    fun getDiffMonthCount(targetCalendar: Calendar, currentCalendar: Calendar): Int {
        val num = targetCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)
        val yearNum = (targetCalendar.get(Calendar.YEAR) - currentCalendar.get(Calendar.YEAR)) * 12
        return num + yearNum
    }

    /**
     * 获取当前两个短间距时间相隔多少周 (使用限制-上下周期一年内)
     * 适用于点击/翻页 跟随
     */
    @SuppressLint("SimpleDateFormat")
    fun getDiffWeekCount(tagCalendar: Calendar, currentCalendar: Calendar): Int {
        val tag = setSundayOfWeek(tagCalendar)
        val current = setSundayOfWeek(currentCalendar)
        val df = SimpleDateFormat("yyyy-MM-dd")
        println("${tag.get(Calendar.YEAR)}-${tag.get(Calendar.MONTH)}-${tag.get(Calendar.DATE)}")
        println("${current.get(Calendar.YEAR)}-${current.get(Calendar.MONTH)}-${current.get(Calendar.DATE)}")
        val startTime =
            df.parse("${tag.get(Calendar.YEAR)}-${tag.get(Calendar.MONTH)}-${tag.get(Calendar.DATE)}")?.time
                ?: 0
        val endTime = df.parse(
            "${current.get(Calendar.YEAR)}-${current.get(Calendar.MONTH)}-${current.get(Calendar.DATE)}"
        )?.time ?: 0
        val weekTime = (1000 * 3600 * 24 * 7)
        val diff = ((startTime - endTime) % weekTime).toInt()
        val diffValue = if (Math.abs(diff) >= (weekTime / 2)) 1 else 0
        val count = ((startTime - endTime) / weekTime).toInt()
        return if (count > 0) count + diffValue else if (count < 0) count - diffValue else diffValue
    }

    /**
     * 获取两个时间 长间距相隔多少个周  （使用限制条件：起始时间和结束时间同为周天）
     * 适用于初始化 month/week 对比 同步page
     */
    fun getDiffWeekPage(tagCalendar: Calendar, currentCalendar: Calendar): Int {
        val diff = tagCalendar.timeInMillis - currentCalendar.timeInMillis
        val nd = 1000 * 24 * 60 * 60.toLong()
        val day = diff / nd
        val week = day / 7
        return week.toInt()
    }

    /**
     * 设置当前时间为当月第一天
     */
    fun setFirstDayOfMonth(calendar: Calendar): Calendar {
        calendar.set(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    /**
     * 将日历设置为当周的第一天 (周日)
     */
    fun setFirstDayOfWeek(calendar: Calendar): Calendar {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    /**
     * 将当前日期设置为这周第一天 (周日)
     * @return 日历对象
     */
    fun setSundayOfWeek(calendar: Calendar): Calendar {
        return calendar.also {
            it.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - it.get(Calendar.DAY_OF_WEEK))
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
        }
    }

    /**
     * 获取当月最大天数
     * @param year 年 1995
     * @param month 0-11
     */
    fun getMaxDayOfMonth(calendar: Calendar,year: Int, month: Int): Int {
        //日期设置为当月第一天
        calendar.set(year, month, 1)
        //日期回滚一天，为当月最后一天
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     * 获取当年最大周数
     * @param year 年 1995
     */
    fun getMaxWeekOfYear(year: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year + 1, 0, 1)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        if (weekNumber == 1) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
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
    fun firstDayIsSunday(year: Int, month: Int): Boolean {
        val calendar = Calendar.getInstance().also {
            it.set(year, month, 1)
        }
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    /**
     * 最后一周是否为当前月份
     */
    fun lastWeekDayIsToMonth(year: Int, month: Int): Boolean {
        val lastDayIsWeek = getMonthWeekCount(year, month)
        val calendar = Calendar.getInstance().also {
            it.set(year, month, 1)
            it.set(Calendar.WEEK_OF_MONTH, lastDayIsWeek)
            it.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        }
        return calendar.get(Calendar.MONTH) == month
    }

    /**
     * 获取当前月总周数
     */
    fun getMonthWeekCount(year: Int, month: Int): Int {
        val day = getMaxDayOfMonth(Calendar.getInstance(),year, month)
        val calendar = Calendar.getInstance().also {
            it.set(year, month, day)
        }
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }

    /**
     * 根据年月获取当天日期， 非当年当月返回 -1
     */
    fun getTodayNumber(calendar: Calendar, year: Int, month: Int): Int {
        val isToMonth = calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month
        return if (isToMonth) calendar.get(Calendar.DAY_OF_MONTH) else -1
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

    fun getEnglishMonth(month: Int): String {
        return when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "Aug"
            8 -> "Sept"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> ""
        }
    }

}