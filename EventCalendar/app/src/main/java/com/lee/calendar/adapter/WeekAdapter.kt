package com.lee.calendar.adapter

import com.lee.calendar.R
import com.lee.calendar.entity.DateData
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.widget.MonthView

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description
 */
class WeekAdapter(data:ArrayList<DateEntity>) :BaseCalendarPageAdapter(data){
    override fun monthMode(): Int {
        return MonthView.MonthMode.MODE_WEEK
    }

    override fun getItemLayout(): Int {
        return R.layout.item_month_view
    }

    fun updateDayStatus(position: Int, arrayList: ArrayList<DateData>) {
        if (getData().isEmpty() || getData().size < position) {
            return
        }
        val monthEntity = getData()[position]
        for ((index, item) in arrayList.withIndex()) {
            if (monthEntity.dayList.size > index) {
                val dayEntity = monthEntity.dayList[index]
                dayEntity.dayStatus = item.status
                dayEntity.backgroundStatus = item.backgroundStatus
            }
        }
        notifyItemChanged(position)
    }
}