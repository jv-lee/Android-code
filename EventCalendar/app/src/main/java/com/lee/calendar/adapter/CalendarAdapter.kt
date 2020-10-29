package com.lee.calendar.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity

/**
 * @author jv.lee
 * @date 2020/10/29
 * @description
 */
class CalendarAdapter(context: Context) : CalendarMonthPageAdapter(context) {

    override fun getItemLayout(): Int {
        return R.layout.item_day
    }

    override fun convert(context: Context, itemView: View, position: Int, entity: DayEntity) {
        val tvDayText = itemView.findViewById<TextView>(R.id.tv_day_text)
        tvDayText?.text = entity.day.toString()
    }

}