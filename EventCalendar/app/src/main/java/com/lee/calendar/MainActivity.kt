package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.adapter.CalendarAdapter
import com.lee.calendar.adapter.CalendarMonthPageAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.utils.DensityUtil

class MainActivity : AppCompatActivity() {

    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress) }
    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }
    private val rvContainer by lazy { findViewById<RecyclerView>(R.id.rv_container) }
    private val mAdapter by lazy {
        CalendarAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DensityUtil.setDensity(this)
        setContentView(R.layout.activity_main)

        mAdapter.setOnChangeDataListener(object : CalendarMonthPageAdapter.OnChangeDataListener {
            override fun onChangeDate(position:Int,entity:DateEntity) {
//                progressBar.visibility = View.GONE
//                rvContainer.visibility = View.VISIBLE
                tvDateDescription.text =  "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i("MonthData", "onChangeDate: $position - ${entity.year}-${entity.month}")
            }
        })
        mAdapter.bindRecyclerView(rvContainer)
    }
}