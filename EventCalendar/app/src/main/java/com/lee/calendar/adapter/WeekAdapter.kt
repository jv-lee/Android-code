package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.entity.MonthData
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/11/5
 * @description
 */
class WeekAdapter :WeekPageAdapter(){
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

    fun updateDayStatus(position: Int, arrayList: ArrayList<MonthData>) {
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