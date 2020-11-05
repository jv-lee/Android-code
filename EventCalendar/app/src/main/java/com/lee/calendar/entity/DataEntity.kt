package com.lee.calendar.entity

import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class DataEntity(
    val year: Int,
    val month: Int,
    val dayList: ArrayList<DayEntity>,
    val startIndex: Int
) {
    companion object {
        fun parseMonthEntity(year: Int, month: Int): DataEntity {
            val calendar = Calendar.getInstance()
            //设置时间为当月1号
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, 1)

            //获取当月1号遍历数量
            val startIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
            return DataEntity(year, month, CalendarUtils.getMonthDayList(year, month,startIndex), startIndex)
        }

        fun parseWeekEntity(year: Int,month: Int,day:Int):DataEntity{
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DATE,day)
            return DataEntity(year,month,CalendarUtils.getWeekDayList(year,month,day),startIndex = -1)
        }
    }

    override fun toString(): String {
        return "{${year}-${month}-${dayList}}"
    }
}