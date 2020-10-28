package com.lee.calendar

import com.lee.calendar.entity.MonthEntity
import java.util.*

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
class CalendarManager {

    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var prevMonthPosition: Int = 0
    private var nextMonthPosition: Int = 0

    /**
     * 移动基础下标 从移动后的基础下标开始获取日历对象
     * @param movePosition 日期偏移量
     */
    private fun getMoveMonthData(movePosition: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.add(Calendar.MONTH, movePosition)
        return calendar
    }

    /**
     * 初始化首页数据
     */
    fun getInitMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        monthArray.add(MonthEntity.parseMonthEntity(currentYear, currentMonth))
        monthArray.addAll(0, getPrevMonthData())
        monthArray.addAll(getNextMonthData())
        return monthArray
    }

    /**
     * 向前填充月份数据
     */
    fun getPrevMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        for (index in 1..6) {
            //获取向上$index月时间对象
            val prevCalendar = getMoveMonthData(-(++prevMonthPosition))
            //添加数据
            monthArray.add(
                0,
                MonthEntity.parseMonthEntity(
                    prevCalendar.get(Calendar.YEAR),
                    prevCalendar.get(Calendar.MONTH)
                )
            )
        }
        return monthArray
    }

    /**
     * 向后填充月份数据
     */
    fun getNextMonthData(): ArrayList<MonthEntity> {
        val monthArray = arrayListOf<MonthEntity>()
        for (index in 1..6) {
            //获取向下$index月时间对象
            val nextCalendar = getMoveMonthData(++nextMonthPosition)
            //添加数据
            monthArray.add(
                MonthEntity.parseMonthEntity(
                    nextCalendar.get(Calendar.YEAR),
                    nextCalendar.get(Calendar.MONTH)
                )
            )
        }
        return monthArray
    }

}