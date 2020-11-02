package com.lee.calendar.entity

import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class MonthEntity(
    val year: Int,
    val month: Int,
    val dayList: ArrayList<DayEntity>,
    val startIndex: Int
) {
    companion object {
        fun parseMonthEntity(year: Int, month: Int): MonthEntity {
            val calendar = Calendar.getInstance()
            //设置时间为当月1号
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, 1)

            //获取当月1号遍历数量
            val startIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
            return MonthEntity(year, month, CalendarUtils.getDayList(year, month,startIndex,Calendar.getInstance().get(Calendar.MONTH) == month), startIndex)
        }
    }

    override fun toString(): String {
        return "{${year}-${month}}"
    }
}