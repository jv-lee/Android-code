package com.lee.calendar

import android.annotation.SuppressLint
import com.lee.calendar.widget.calendar.utils.CalendarUtils
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jv.lee
 * @date 2020/12/14
 * @description
 */
class WeekDiffTest {
    @Test
    fun test() {
//        val tag = CalendarUtils.setWeekToSunday(2020,10,5)
//        var tag = Calendar.getInstance().also {
//            it.set(Calendar.YEAR, 2020)
//            it.set(Calendar.MONTH, 10)
//            it.set(Calendar.DAY_OF_MONTH, 5)
//        }
//        tag = CalendarUtils.setWeekToSunday(
//            tag.get(Calendar.YEAR),
//            tag.get(Calendar.MONTH),
//            tag.get(Calendar.DAY_OF_MONTH)
//        )
//        val current = Calendar.getInstance().also {
//            CalendarUtils.setWeekToSunday(it)
//        }
//        val diff = getDiffWeekPage(tag, current)
//        println("${tag.get(Calendar.MONTH)}:${tag.get(Calendar.DATE)}")
//        println("${current.get(Calendar.MONTH)}:${current.get(Calendar.DATE)}")
//        println(diff)
    }

    @Test
    fun test2() {
        var tag = Calendar.getInstance().also {
            it.set(Calendar.YEAR, 2021)
            it.set(Calendar.MONTH, 0)
            it.set(Calendar.DAY_OF_MONTH, 1)
        }
//        println("${tag.get(Calendar.MONTH)}:${tag.get(Calendar.DATE)}")
        val current = CalendarUtils.setFirstDayOfMonth(Calendar.getInstance())
//        println("${current.get(Calendar.MONTH)}:${current.get(Calendar.DATE)}")

        val diff = getDiffWeekPage(tag, current)

        println(diff)
    }


    /**
     * 获取两个时间 长间距相隔多少个周  （使用限制条件：起始时间和结束时间同为周天）
     * 适用于初始化 month/week 对比 同步page
     */
    fun getDiffWeekPage(tagCalendar: Calendar, currentCalendar: Calendar): Int {

        val tag = CalendarUtils.setFirstDayOfWeek(tagCalendar.also { it.time })
        println("${tag.get(Calendar.YEAR)},${tag.get(Calendar.MONTH)},${tag.get(Calendar.DATE)}")
        val current = CalendarUtils.setFirstDayOfWeek(currentCalendar.also { it.time })
        println("${current.get(Calendar.YEAR)},${current.get(Calendar.MONTH)},${current.get(Calendar.DATE)}")
        val diff = tag.timeInMillis - current.timeInMillis
        val nd = 1000 * 24 * 60 * 60.toLong()
        val day = diff / nd
        println(day)
        val week = day / 7
        println(week.toInt())
        return week.toInt()
    }

    @Test
    fun textTime() {
        println(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(1639267200000)))
    }

    @Test
    fun testDiffTime() {
        var tag = CalendarUtils.setFirstDayOfWeek(Calendar.getInstance().apply { set(2020, 9, 2) })
//        var tag = CalendarUtils.setFirstDayOfWeek(2021, 0, 3)
        println(getDiffWeek(tag, CalendarUtils.setFirstDayOfWeek(Calendar.getInstance())))
    }


    @SuppressLint("SimpleDateFormat")
    fun getDiffWeek(tagCalendar: Calendar, currentCalendar: Calendar): Int {
        val tag = CalendarUtils.setFirstDayOfWeek(tagCalendar)
        val current = CalendarUtils.setFirstDayOfWeek(currentCalendar)
        val df = SimpleDateFormat("yyyy-MM-dd")
        println("${tag.get(Calendar.YEAR)}-${tag.get(Calendar.MONTH)}-${tag.get(Calendar.DATE)}")
        println("${current.get(Calendar.YEAR)}-${current.get(Calendar.MONTH)}-${current.get(Calendar.DATE)}")
        val startTime =
            df.parse("${tag.get(Calendar.YEAR)}-${tag.get(Calendar.MONTH)}-${tag.get(Calendar.DATE)}")?.time
                ?: 0
        val endTime = df.parse(
            "${current.get(Calendar.YEAR)}-${current.get(Calendar.MONTH)}-${current.get(Calendar.DATE)}"
        )?.time ?: 0
        val weekTime = (1000 * 3600 * 24 * 7)
//        println(weekTime)
//        println(startTime - endTime)
//        println(((startTime - endTime) % weekTime).toInt())
        val diff = ((startTime - endTime) % weekTime).toInt()
        val diffValue = if (Math.abs(diff) >= (weekTime / 2)) 1 else 0
        val count = ((startTime - endTime) / weekTime).toInt()
        return if (count > 0) count + diffValue else if (count < 0) count + -diffValue else count
    }

}