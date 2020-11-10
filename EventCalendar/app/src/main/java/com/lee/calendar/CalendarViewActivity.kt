package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lee.calendar.adapter.MonthAdapter
import com.lee.calendar.adapter.WeekAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.viewmodel.TestViewModel
import com.lee.calendar.widget.CalendarView

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description
 */
class CalendarViewActivity : AppCompatActivity(R.layout.activity_calendar_view) {

    private val TAG = CalendarViewActivity::class.java.simpleName

    private val viewModel by lazy { ViewModelProviders.of(this).get(TestViewModel::class.java) }

    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }
    private val linearContainer by lazy { findViewById<LinearLayout>(R.id.linear_container) }
    private val calendarView by lazy { findViewById<CalendarView>(R.id.calendar_view) }

    private val monthPagerAdapter by lazy { MonthAdapter() }
    private val weekPagerAdapter by lazy { WeekAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendarView.setOnChangePager(object:CalendarView.OnChangePager{
            override fun onMonthPageChange(position: Int, entity: DateEntity,viewVisibility:Int) {
                Log.i(TAG, "onMonthPageChange: $position - ${entity.year}-${entity.month}")
                viewModel.getMonthData(position, entity.year, entity.month)
            }

            override fun onWeekPageChange(position: Int, entity: DateEntity,viewVisibility:Int) {
                Log.i(TAG, "onWeekPageChange: $position - ${entity.year}-${entity.month}")
            }

            override fun onDayChange(position: Int, entity: DayEntity) {
                tvDateDescription.text = "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i(TAG, "onDayChange: $position - $entity")
            }

        })
        calendarView.bindAdapter(weekPagerAdapter,monthPagerAdapter)
        calendarView.addContainerTouch(linearContainer)

        viewModel.monthLiveData.observe(this, Observer {
            monthPagerAdapter.updateDayStatus(it.position, it.data)
        })

    }
}