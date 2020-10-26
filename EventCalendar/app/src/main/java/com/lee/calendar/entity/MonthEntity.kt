package com.lee.calendar.entity

/**
 * @author jv.lee
 * @date 2020/10/26
 * @description
 */
data class MonthEntity(val year: Int, val month: Int, val dayList: ArrayList<DayEntity>? = null)