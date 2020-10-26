package com.lee.calendar.utils

import java.util.*

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
object CalendarUtils {

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

    fun getDayOfWeek(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

}