package com.lee.calendar.adapter

import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.manager.CalendarManager2
import com.lee.calendar.manager.ICalendarData
import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
abstract class MonthPageAdapter :BaseCalendarPageAdapter(){
    override fun createCalendarManager(): ICalendarData {
        return CalendarManager2(
            2020, 0, 1,
            loadMoreMonthCount = 12
        )
    }

    override fun initListData(): ArrayList<DateEntity> {
        return calendarManager.initMonthList()
    }

    override fun loadMoreListData(): ArrayList<DateEntity> {
        return calendarManager.loadMoreMonthList()
    }

    override fun selectItem(entity: DayEntity) {
        currentDay ?: return
        val calendar = Calendar.getInstance()
        calendar.set(entity.year, entity.month, entity.day)

        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)

        val intervalMonthCount = CalendarUtils.getIntervalMonthCount(calendar, currentCalendar)

        val currentItemIndex = viewPager?.currentItem!! + intervalMonthCount
        viewPager?.setCurrentItem(currentItemIndex, true)

        dayListAdapterMap[currentItemIndex]?.let {
            for ((index, day) in it.data.withIndex()) {
                if (day.day == entity.day && day.isToMonth) {
                    it.selectItemByPosition(index, it.data[index])
                    it.initSelectRowIndex(index, day)
                }
            }
        }
    }
}