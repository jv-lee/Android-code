package com.lee.calendar

import com.lee.calendar.entity.MonthEntity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
class CalendarManager {

    companion object {
        @Volatile
        private var instance: CalendarManager? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: CalendarManager().also { instance = it }
        }
    }

    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var prevMonthPosition: Int = 1
    private var nextMonthPosition: Int = 1

    fun getInitMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        monthArray.add(MonthEntity(currentYear, currentMonth))
        for (index in 1..6) {
            //修改本地月份下标起始位置
            prevMonthPosition = index
            nextMonthPosition = index
            //获取向上$index月时间对象
            val prevCalendar = getMoveMonthData(-index)
            //获取向下$index月时间对象
            val nextCalendar = getMoveMonthData(index)
            //添加数据
            monthArray.add(0, MonthEntity(prevCalendar.get(Calendar.YEAR),prevCalendar.get(Calendar.MONTH)))
            monthArray.add(MonthEntity(nextCalendar.get(Calendar.YEAR),nextCalendar.get(Calendar.MONTH)))
        }
        return monthArray
    }

    /**
     * 向前填充月份数据
     */
    fun getPrevMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        val position = prevMonthPosition + 1
        for (index in position..(position + 5)) {
            //修改本地月份下标起始位置
            prevMonthPosition = index
            //获取向上$index月时间对象
            val prevCalendar = getMoveMonthData(-index)
            //添加数据
            monthArray.add(0, MonthEntity(prevCalendar.get(Calendar.YEAR),prevCalendar.get(Calendar.MONTH)))
        }
        return monthArray
    }

    /**
     * 获取下一月数据
     */
    fun getNextMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        val position = nextMonthPosition + 1
        for (index in position..(position + 5)) {
            //修改本地月份下标起始位置
            nextMonthPosition = index
            //获取向下$index月时间对象
            val nextCalendar = getMoveMonthData(index)
            //添加数据
            monthArray.add(MonthEntity(nextCalendar.get(Calendar.YEAR),nextCalendar.get(Calendar.MONTH)))
        }
        return monthArray
    }

    private fun getMoveMonthData(movePosition: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.add(Calendar.MONTH, movePosition)
        return calendar
    }

}