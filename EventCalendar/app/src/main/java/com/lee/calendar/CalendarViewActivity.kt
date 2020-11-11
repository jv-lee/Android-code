package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.adapter.MonthAdapter
import com.lee.calendar.adapter.TestDataAdapter
import com.lee.calendar.adapter.WeekAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.viewmodel.TestViewModel
import com.lee.calendar.widget.CalendarView
import com.lee.calendar.widget.CalendarLinearLayout

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description
 */
class CalendarViewActivity : AppCompatActivity(R.layout.activity_calendar_view) {

    private val TAG = CalendarViewActivity::class.java.simpleName

    private val viewModel by lazy { ViewModelProviders.of(this).get(TestViewModel::class.java) }

    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }
    private val linearContainer by lazy { findViewById<CalendarLinearLayout>(R.id.linear_container) }
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
        linearContainer.bindEventView(calendarView,rvContainer)

        viewModel.monthLiveData.observe(this, Observer {
            monthPagerAdapter.updateDayStatus(it.position, it.data)
        })

        initRecyclerViewData()
    }

    private val rvContainer by lazy { findViewById<RecyclerView>(R.id.rv_container) }
    private val recyclerAdapter by lazy { TestDataAdapter(arrayListOf<String>().also {
        for (index in 0..30) {
            it.add("this is content - position($index)")
        }
    }) }

    private fun initRecyclerViewData(){
        rvContainer.layoutManager = LinearLayoutManager(this)
        rvContainer.adapter = recyclerAdapter
    }

}