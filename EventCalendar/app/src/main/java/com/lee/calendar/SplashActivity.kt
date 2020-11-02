package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lee.calendar.adapter.CalendarAdapter
import com.lee.calendar.adapter.MonthPageAdapter
import com.lee.calendar.entity.DayEntity
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
            override fun onPageChangeDate(position: Int, entity: MonthEntity) {
                tvDateDescription.text =
                    "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i(
                    "Pager",
                    "onChangeDate: $position - ${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                )
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                Toast.makeText(this@SplashActivity, "position:$position , ${entity.year}-${entity.month}-${entity.day}", Toast.LENGTH_SHORT).show()
            }
        })
        monthPagerAdapter.bindViewPager(vpContainer)

    }
}