package com.lee.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lee.calendar.utils.DensityUtil

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DensityUtil.setDensity(this)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_calendar_view).setOnClickListener {
            startActivity(Intent(this@MainActivity, CalendarViewActivity::class.java))
        }

        findViewById<Button>(R.id.btn_month_view).setOnClickListener {
            startActivity(Intent(this@MainActivity, MonthViewActivity::class.java))
        }

    }
}