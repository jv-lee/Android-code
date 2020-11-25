package com.lee.calendar.utils

import com.lee.calendar.entity.DateData
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.widget.calendar.MonthView
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/17
 * @description
 */
object AttendanceDataUtils {

    /**
     * 是否使用缓存
     * 小于当月 返回true
     * 大于等于当月 返回false
     */
    fun hasCache(year: Int, month: Int): Boolean {
        val calendar = Calendar.getInstance()
        return if (calendar.get(Calendar.YEAR) == year && month < calendar.get(Calendar.MONTH)) {
            true
        } else calendar.get(Calendar.YEAR) < year
    }

    fun setBackgroundStatus(monthData: List<DateData>?, year: Int, month: Int, hashWeek: Boolean = false): List<DateData>? {
        monthData ?: return null
        for ((index, item) in monthData.withIndex()) {
            paresBackgroundStatus(index, item, monthData, year, month, hashWeek)
        }
        return monthData
    }

    private fun paresBackgroundStatus(
            position: Int,
            dayEntity: DateData,
            arrayList: List<DateData>,
            year: Int,
            month: Int,
            hashWeek: Boolean
    ) {
        val isDrawGone = isDrawGone(dayEntity)
        if (isDrawGone) {
            dayEntity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_GONE
            return
        }

        val isDrawSingle = isDrawSingle(position, dayEntity, arrayList)
        if (isDrawSingle) {
            dayEntity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
            return
        }

        if (hashWeek) {
            isDrawWeekLocation(position, dayEntity, arrayList, year, month)
        } else {
            isDrawLocation(position, dayEntity, arrayList, year, month)
        }
    }

    private fun isDrawGone(entity: DateData): Boolean {
        return entity.status != DayStatus.UPDATE_STATUS && entity.status != DayStatus.OVER_UPDATE_STATUS
    }

    private fun isDrawSingle(
            position: Int,
            entity: DateData,
            arrayList: List<DateData>
    ): Boolean {
        val prevEntity: DateData? = if (position == 0) null else arrayList[position - 1]
        val nextEntity: DateData? =
                if (position == arrayList.size - 1) null else arrayList[position + 1]
        if (prevEntity == null) {
            return entity.status != nextEntity?.status
        }
        if (nextEntity == null) {
            return entity.status != prevEntity.status
        }
        return entity.status != prevEntity.status && entity.status != nextEntity.status
    }

    private fun isDrawLocation(position: Int, entity: DateData, arrayList: List<DateData>, year: Int, month: Int) {
        val prevEntity: DateData? = if (position == 0) null else arrayList[position - 1]
        val nextEntity: DateData? =
                if (position == arrayList.size - 1) null else arrayList[position + 1]
        val week = CalendarUtils.getDayOfWeek(year, month, entity.day)

        //当月第一天
        if (prevEntity == null && entity.status == nextEntity?.status && week != 7) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
            return
        }

        //当月最后一天
        if (nextEntity == null && entity.status == prevEntity?.status && week != 1) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
            return
        }

        if (entity.status != prevEntity?.status && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
        }

        if (entity.status == prevEntity?.status && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_CENTER
        }

        if (entity.status == prevEntity?.status && entity.status != nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
        }

        //周天 / 周六切割
        if (week == 1 && entity.status == nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
        }
        if (week == 1 && entity.status != nextEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
        }
        if (week == 7 && entity.status == prevEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
        }
        if (week == 7 && entity.status != prevEntity?.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
        }
    }

    private fun isDrawWeekLocation(position: Int, entity: DateData, arrayList: List<DateData>, year: Int, month: Int) {
        val prevEntity: DateData? = if (position == 0) null else arrayList[position - 1]
        val nextEntity: DateData? =
                if (position == arrayList.size - 1) null else arrayList[position + 1]

        //星期天
        if (prevEntity == null) {
            if (entity.status != nextEntity?.status) {
                entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
            } else if (entity.status == nextEntity.status) {
                entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
            }
            return
        }

        //星期六
        if (nextEntity == null) {
            if (entity.status != prevEntity.status) {
                entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_SINGLE
            } else if (entity.status == prevEntity.status) {
                entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
            }
            return
        }

        //周内
        if (entity.status != prevEntity.status && entity.status == nextEntity.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_START
        }

        if (entity.status == prevEntity.status && entity.status == nextEntity.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_CENTER
        }

        if (entity.status == prevEntity.status && entity.status != nextEntity.status) {
            entity.backgroundStatus = MonthView.DayBackgroundStatus.STATUS_END
        }

    }
}