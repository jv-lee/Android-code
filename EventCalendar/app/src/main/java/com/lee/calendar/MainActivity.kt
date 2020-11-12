package com.lee.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.adapter.CalendarAdapter
import com.lee.calendar.adapter.CalendarMonthPageAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.utils.DensityUtil

class MainActivity : AppCompatActivity() {

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
                tvDateDescription.text =  "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
            }
        })
        mAdapter.bindRecyclerView(rvContainer)

        tvDateDescription.setOnClickListener {
            startActivity(Intent(this@MainActivity,CalendarViewActivity::class.java))
        }
    }
}