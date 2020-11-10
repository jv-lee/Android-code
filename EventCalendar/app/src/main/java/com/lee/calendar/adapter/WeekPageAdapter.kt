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
abstract class WeekPageAdapter : BaseCalendarPageAdapter() {
    override fun createCalendarManager(): ICalendarData {
        return CalendarManager2(
            2020, 0, 1,
            loadMoreMonthCount = 12
        )
    }

    override fun initListData(): ArrayList<DateEntity> {
        return calendarManager.initWeekList()
    }

    override fun loadMoreListData(): ArrayList<DateEntity> {
        return calendarManager.loadMoreWeekList()
    }

    override fun selectItem(entity: DayEntity) {
        currentDay ?: return
        val calendar = Calendar.getInstance()

        calendar.set(entity.year, entity.month, entity.day)
        val tagWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val tagMaxWeek = CalendarUtils.getMaxWeekCountByYear(entity.year)

        calendar.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentMaxWeek = CalendarUtils.getMaxWeekCountByYear(currentDay?.year!!)

        val week = if (tagWeek < 10 && currentWeek > 46) {
            tagWeek + (currentMaxWeek - currentWeek)
        } else if (tagWeek > 46 && currentWeek < 10) {
            -(currentWeek + (tagMaxWeek - tagWeek))
        } else {
            tagWeek - currentWeek
        }
        val index = viewPager?.currentItem!! + week
        viewPager?.setCurrentItem(index, true)

        if (dayListAdapterMap[index] == null) {
            currentDay = entity
        }
        dayListAdapterMap[index]?.let {
            for ((index, day) in it.data.withIndex()) {
                if (day.day == entity.day) {
                    it.selectItemByPosition(index, it.data[index])
                }
            }
        }
    }
}