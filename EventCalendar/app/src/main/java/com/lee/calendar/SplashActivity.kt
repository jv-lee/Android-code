package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lee.calendar.adapter.CalendarAdapter
import com.lee.calendar.adapter.MonthPageAdapter
import com.lee.calendar.entity.MonthEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.widget.VerticalViewPager

class SplashActivity : AppCompatActivity() {

    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }
    private val vpContainer by lazy { findViewById<VerticalViewPager>(R.id.vp_container) }
    private val monthPagerAdapter by lazy { CalendarAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        monthPagerAdapter.setOnChangeDataListener(object : MonthPageAdapter.OnChangeDataListener {
            override fun onChangeDate(position: Int, entity: MonthEntity) {
                tvDateDescription.text =
                    "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i(
                    "Pager",
                    "onChangeDate: $position - ${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                )
            }
        })
        monthPagerAdapter.bindViewPager(vpContainer)

    }
}