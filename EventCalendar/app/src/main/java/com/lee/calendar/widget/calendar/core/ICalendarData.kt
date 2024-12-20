package com.lee.calendar.widget.calendar.core

import com.lee.calendar.widget.calendar.entity.DateEntity

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
interface ICalendarData {
    fun initDateList(): ArrayList<DateEntity>
    fun loadNextDateList(): ArrayList<DateEntity>
    fun loadPrevDateList(): ArrayList<DateEntity>
}