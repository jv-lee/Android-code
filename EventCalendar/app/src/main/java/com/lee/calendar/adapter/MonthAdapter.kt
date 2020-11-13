package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.entity.DateData
import com.lee.calendar.manager.CalendarManager
import com.lee.calendar.manager.ICalendarData
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class MonthAdapter : BaseCalendarPageAdapter() {
    override fun createCalendarManager(): ICalendarData {
        return CalendarManager(
            2018, 0, 1,
            endMonth = 12
        )
    }

    override fun isMonthMode(): Boolean {
        return true
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
            if (monthEntity.dayList.size > index + monthEntity.startIndex) {
                val dayEntity = monthEntity.dayList[index + monthEntity.startIndex]
                dayEntity.dayStatus = item.status
                dayEntity.backgroundStatus = item.backgroundStatus
            }
        }
        dayListAdapterMap[position]?.notifyDataSetChanged()
    }

}