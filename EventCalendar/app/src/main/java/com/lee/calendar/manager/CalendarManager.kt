package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
class CalendarManager(val prevMonthCount: Int = 6,val nextMonthCount: Int = 6) {

    private var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private var prevMonthPosition: Int = 0
    private var nextMonthPosition: Int = 0

    private var prevWeekPosition:Int = 0
    private var nextWeekPosition:Int = 0

    private val startIndex = 1

    private fun getMoveWeekData(movePosition:Int):Calendar{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH,currentDay)
        calendar.add(Calendar.DAY_OF_MONTH, movePosition * 7)
        return calendar
    }

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

    fun getInitWeekData():ArrayList<DateEntity>{
        val weekArray = arrayListOf<DateEntity>()
        weekArray.add(DateEntity.parseWeekEntity(currentYear,currentMonth,currentDay))
        weekArray.addAll(0,getPrevWeekData())
        weekArray.addAll(getNextWeekData())
        return weekArray
    }

    fun getPrevWeekData():ArrayList<DateEntity>{
        val dataArray = arrayListOf<DateEntity>()
        for (index in startIndex..prevMonthCount * 4) {
            //获取向上$index周时间对象
            val prevCalendar = getMoveWeekData(-(++prevWeekPosition))
            //添加数据
            dataArray.add(
                0,
                DateEntity.parseWeekEntity(
                    prevCalendar.get(Calendar.YEAR),
                    prevCalendar.get(Calendar.MONTH),
                    prevCalendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        return dataArray
    }

    fun getNextWeekData():ArrayList<DateEntity>{
        val dataArray = arrayListOf<DateEntity>()
        for (index in startIndex..nextMonthCount * 4) {
            //获取向上$index周时间对象
            val nextCalendar = getMoveWeekData((++nextWeekPosition))
            //添加数据
            dataArray.add(
                DateEntity.parseWeekEntity(
                    nextCalendar.get(Calendar.YEAR),
                    nextCalendar.get(Calendar.MONTH),
                    nextCalendar.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
        return dataArray
    }

    /**
     * 初始化首页数据
     */
    fun getInitMonthData(): ArrayList<DateEntity> {
        val monthArray = arrayListOf<DateEntity>()
        monthArray.add(DateEntity.parseMonthEntity(currentYear, currentMonth))
        monthArray.addAll(0, getPrevMonthData())
        monthArray.addAll(getNextMonthData())
        return monthArray
    }

    /**
     * 向前填充月份数据
     */
    fun getPrevMonthData(): ArrayList<DateEntity> {
        val monthArray = arrayListOf<DateEntity>()
        for (index in startIndex..prevMonthCount) {
            //获取向上$index月时间对象
            val prevCalendar = getMoveMonthData(-(++prevMonthPosition))
            //添加数据
            monthArray.add(
                0,
                DateEntity.parseMonthEntity(
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
    fun getNextMonthData(): ArrayList<DateEntity> {
        val monthArray = arrayListOf<DateEntity>()
        for (index in startIndex..nextMonthCount) {
            //获取向下$index月时间对象
            val nextCalendar = getMoveMonthData(++nextMonthPosition)
            //添加数据
            monthArray.add(
                DateEntity.parseMonthEntity(
                    nextCalendar.get(Calendar.YEAR),
                    nextCalendar.get(Calendar.MONTH)
                )
            )
        }
        return monthArray
    }

}