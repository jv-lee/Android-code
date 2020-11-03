package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DayStatus
import com.lee.calendar.entity.MonthData
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class CalendarAdapter : MonthPageAdapter() {

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
            if (monthEntity.dayList.size >= index + monthEntity.startIndex) {
                val dayEntity = monthEntity.dayList[index + monthEntity.startIndex]
                dayEntity.dayStatus = item.status
                paresBackgroundStatus(index,dayEntity,arrayList)
            }
        }
        dayListAdapterMap[position]?.notifyItemRangeChanged(0, monthEntity.dayList.size)
    }

    private fun paresBackgroundStatus(position:Int,dayEntity: DayEntity, arrayList: ArrayList<MonthData>) {
        val isDrawGone = isDrawGone(dayEntity)
        if(isDrawGone){
            dayEntity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_GONE
            return
        }

        val isDrawSingle = isDrawSingle(position,dayEntity,arrayList)
        if (isDrawSingle) {
            dayEntity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_SINGLE
            return
        }

        isDrawLocation(position,dayEntity,arrayList)
    }

    private fun isDrawGone(entity: DayEntity):Boolean {
        return entity.dayStatus != DayStatus.UPDATE_STATUS && entity.dayStatus != DayStatus.OVER_UPDATE_STATUS
    }

    private fun isDrawSingle(position: Int,entity: DayEntity,arrayList: ArrayList<MonthData>):Boolean {
        val prevEntity :MonthData? = if(position == 0) null else arrayList[position-1]
        val nextEntity :MonthData? = if(position == arrayList.size - 1) null else arrayList[position + 1]
        if (prevEntity == null) {
            return entity.dayStatus != nextEntity?.status
        }
        if (nextEntity == null) {
            return entity.dayStatus != prevEntity.status
        }
        return entity.dayStatus != prevEntity.status && entity.dayStatus != nextEntity.status
    }

    private fun isDrawLocation(position: Int, entity: DayEntity, arrayList: ArrayList<MonthData>) {
        val prevEntity :MonthData? = if(position == 0) null else arrayList[position-1]
        val nextEntity :MonthData? = if(position == arrayList.size - 1) null else arrayList[position + 1]

        //当月第一天
        if (prevEntity == null && entity.dayStatus == nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_START
            return
        }

        //当月最后一天
        if (nextEntity == null && entity.dayStatus == prevEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_END
            return
        }

        if (entity.dayStatus != prevEntity?.status && entity.dayStatus == nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_START
        }

        if (entity.dayStatus == prevEntity?.status && entity.dayStatus == nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_CENTER
        }

        if (entity.dayStatus == prevEntity?.status && entity.dayStatus != nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_END
        }

        //周天 / 周六切割
        val week = CalendarUtils.getDayOfWeek(entity.year, entity.month, entity.day)
        if (week == 1 && entity.dayStatus == nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_START
        }
        if (week == 1 && entity.dayStatus != nextEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_SINGLE
        }
        if (week == 7 && entity.dayStatus == prevEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_END
        }
        if (week == 7 && entity.dayStatus != prevEntity?.status) {
            entity.backgroundStatus = DayView.DayBackgroundStatus.STATUS_SINGLE
        }
    }

}