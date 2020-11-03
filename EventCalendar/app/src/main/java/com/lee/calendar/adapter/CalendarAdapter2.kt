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
class CalendarAdapter2(context: Context) : CalendarMonthPageAdapter(context) {

    override fun getItemLayout(): Int {
        return R.layout.item_day
    }

    override fun convert(context: Context, itemView: View, position: Int, entity: DayEntity) {
        val dayView = itemView.findViewById<DayView>(R.id.day_view)

        if (position == 0) {
            dayView.updateBackgroundStatus(DayView.DayBackgroundStatus.STATUS_SINGLE)
        }else if (position == 1) {
            dayView.updateBackgroundStatus(DayView.DayBackgroundStatus.STATUS_START)
        }else if(position == 2){
            dayView.updateBackgroundStatus(DayView.DayBackgroundStatus.STATUS_CENTER)
        }else if (position == 3) {
            dayView.updateBackgroundStatus(DayView.DayBackgroundStatus.STATUS_END)
        }else{
            dayView.updateBackgroundStatus(DayView.DayBackgroundStatus.STATUS_SINGLE)
        }

        dayView.setText("12")
    }

}