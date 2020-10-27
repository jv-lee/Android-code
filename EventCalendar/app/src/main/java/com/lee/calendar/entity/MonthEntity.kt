package com.lee.calendar.entity

import com.lee.calendar.utils.CalendarUtils

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class MonthEntity(val year: Int, val month: Int, val dayList: ArrayList<DayEntity>? = null) {
    companion object {
        fun parseMonthEntity(year: Int, month: Int): MonthEntity {
            return MonthEntity(year, month, getDayList(year, month))
        }

        private fun getDayList(year: Int, month: Int): ArrayList<DayEntity> {
            val dayArray = arrayListOf<DayEntity>()
            val dayCount = CalendarUtils.getMonthMaxDay(year, month)
            for (index in 1..dayCount) {
                dayArray.add(DayEntity(year = year, month = month, day = index))
            }
            return dayArray
        }
    }
}