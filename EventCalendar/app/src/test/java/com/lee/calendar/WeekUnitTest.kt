package com.lee.calendar

import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeekUnitTest {
    @Test
    fun addition_isCorrect() {
        val calendar = Calendar.getInstance()
//        val week = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.set(Calendar.DATE, 1)
        //日期回滚一天，为当月最后一天
        calendar.roll(Calendar.DATE, -1)
        println(calendar.get(Calendar.DAY_OF_MONTH))
    }

}