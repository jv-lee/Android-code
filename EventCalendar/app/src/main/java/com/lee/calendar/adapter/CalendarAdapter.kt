package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.widget.DayView

/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class CalendarAdapter : MonthPageAdapter() {

    private var selectedPosition: Int = 0

    override fun getItemLayout(): Int {
        return R.layout.item_day
    }

    override fun convert(context: Context, itemView: View, position: Int, entity: DayEntity) {
        val dayView = itemView.findViewById<DayView>(R.id.day_view)

        //设置当日日期显示
        dayView.setText(entity.day.toString())

        //设置非本月 及当日状态
        dayView.updateIsGone(!entity.isToMonth)
        dayView.updateIsToday(entity.isToDay)

        //设置选中状态
        dayView.updateSelect(entity.isSelected)

        dayView.updateIsDelayUpdate(true)

        if (position == 0) {
            dayView.updateStatus(DayView.DayBackgroundStatus.STATUS_SINGLE)
        }else if (position == 1) {
            dayView.updateStatus(DayView.DayBackgroundStatus.STATUS_START)
        }else if(position == 2){
            dayView.updateStatus(DayView.DayBackgroundStatus.STATUS_CENTER)
        }else if (position == 3) {
            dayView.updateStatus(DayView.DayBackgroundStatus.STATUS_END)
        }else{
            dayView.updateStatus(DayView.DayBackgroundStatus.STATUS_SINGLE)
        }
    }

    fun clickItemUpdateStatus(position: Int, entity: DayEntity,data:ArrayList<DayEntity>) {
        data[selectedPosition].isSelected = false
        data[position].isSelected = true
    }

}