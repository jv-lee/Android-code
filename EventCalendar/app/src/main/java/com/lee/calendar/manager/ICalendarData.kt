package com.lee.calendar.manager

import com.lee.calendar.entity.DateEntity

/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
interface ICalendarData {
    fun initMonthList(): ArrayList<DateEntity>
    fun initWeekList(): ArrayList<DateEntity>
    fun loadMoreMonthList(): ArrayList<DateEntity>
    fun loadMoreWeekList():ArrayList<DateEntity>
}