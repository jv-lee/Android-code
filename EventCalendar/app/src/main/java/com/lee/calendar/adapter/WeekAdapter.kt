package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.entity.DateData
import com.lee.calendar.manager.CalendarManager2
import com.lee.calendar.manager.ICalendarData
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/11/5
 * @description
 */
class WeekAdapter :BaseCalendarPageAdapter(){
    override fun createCalendarManager(): ICalendarData {
        return CalendarManager2(
            2020, 0, 1,
            loadMoreMonthCount = 12
        )
    }

    override fun isMonthMode(): Boolean {
        return false
    }

    override fun getItemLayout(): Int {
        return R.layout.item_day
    }

    override fun convert(context: Context, itemView: View, position: Int, entity: DayEntity) {
        val dayView = itemView.findViewById<DayView>(R.id.day_view)

        when (entity.dayStatus) {
            DayStatus.UPDATE_STATUS -> {
                dayView.updateDataStatus(
                    text = entity.day.toString(),
                    isGone = !entity.isToMonth,
                    isToday = entity.isToDay,
                    isSelect = entity.isSelected,
                    isDelayUpdate = false,
                    isUpdate = true,
                    backgroundStatus = entity.backgroundStatus
                )
            }
            DayStatus.OVER_UPDATE_STATUS -> {
                dayView.updateDataStatus(
                    text = entity.day.toString(),
                    isGone = !entity.isToMonth,
                    isToday = entity.isToDay,
                    isSelect = entity.isSelected,
                    isDelayUpdate = false,
                    isUpdate = false,
                    backgroundStatus = entity.backgroundStatus
                )
            }
            DayStatus.DELAY_UPDATE_STATUS -> {
                dayView.updateDataStatus(
                    text = entity.day.toString(),
                    isGone = !entity.isToMonth,
                    isToday = entity.isToDay,
                    isSelect = entity.isSelected,
                    isDelayUpdate = true,
                    isUpdate = true,
                    backgroundStatus = entity.backgroundStatus
                )
            }
            DayStatus.EMPTY_STATUS -> {
                dayView.updateDataStatus(
                    text = entity.day.toString(),
                    isGone = !entity.isToMonth,
                    isToday = entity.isToDay,
                    isSelect = entity.isSelected,
                    isDelayUpdate = false,
                    isUpdate = true,
                    backgroundStatus = entity.backgroundStatus
                )
            }
        }
    }

    fun updateDayStatus(position: Int, arrayList: ArrayList<DateData>) {
        if (data.isEmpty() || data.size < position) {
            return
        }
        val monthEntity = data[position]
        for ((index, item) in arrayList.withIndex()) {
            if (monthEntity.dayList.size > index) {
                val dayEntity = monthEntity.dayList[index]
                dayEntity.dayStatus = item.status
                dayEntity.backgroundStatus = item.backgroundStatus
            }
        }
        dayListAdapterMap[position]?.notifyDataSetChanged()
    }
}