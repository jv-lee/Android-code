package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.adapter.CalendarAdapter
import com.lee.calendar.adapter.CalendarMonthPageAdapter
import com.lee.calendar.entity.MonthEntity
import com.lee.calendar.utils.CalendarUtils

class MainActivity : AppCompatActivity() {

    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }
    private val rvContainer by lazy { findViewById<RecyclerView>(R.id.rv_container) }
    private val mAdapter by lazy {
        CalendarAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter.setOnChangeDataListener(object : CalendarMonthPageAdapter.OnChangeDataListener {
            override fun onChangeDate(position:Int,entity:MonthEntity) {
                tvDateDescription.text =  "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i("MonthData", "onChangeDate: $position - ${entity.year}-${entity.month}")
            }
        })
        mAdapter.bindRecyclerView(rvContainer)
    }
}