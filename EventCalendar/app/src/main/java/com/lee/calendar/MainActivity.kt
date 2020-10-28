package com.lee.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.adapter.MonthPageAdapter

class MainActivity : AppCompatActivity() {

    private val rvContainer by lazy { findViewById<RecyclerView>(R.id.rv_container) }
    private val mAdapter by lazy {
        MonthPageAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter.bindRecyclerView(rvContainer)
        mAdapter.setOnChangeDataListener(object : MonthPageAdapter.OnChangeDataListener {
            override fun onChangeDate(year: Int, month: Int) {
//                Log.i("MonthData", "onChangeDate: $year - $month")
            }

            override fun onChangePosition(position: Int) {
//                Log.i("MonthData", "onChangePosition: $position")
            }

        })
    }
}