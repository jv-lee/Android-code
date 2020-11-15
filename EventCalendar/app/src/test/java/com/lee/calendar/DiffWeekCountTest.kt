package com.lee.calendar

import com.lee.calendar.utils.CalendarUtils
import org.junit.Test
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description
 */
class DiffWeekCountTest {

    @Test
    fun test(){

        val weekDiffCount = CalendarUtils.getDiffWeekCount(
            Calendar.getInstance().also {
                it.set(Calendar.DATE, 1)
            }, Calendar.getInstance().also {
                it.set(2018, 9, 28)
            })
        println(weekDiffCount)
    }
}