package com.lee.calendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lee.calendar.adapter.MonthAdapter2
import com.lee.calendar.adapter.WeekAdapter2
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.viewmodel.TestViewModel
import com.lee.calendar.widget.CalendarLinearLayout2
import com.lee.calendar.widget.CalendarView2

/**
 * @author jv.lee
 * @date 2020/11/15
 * @description
 */
class CalendarView2Activity : AppCompatActivity(R.layout.activity_calendar_view2) {

    private val viewModel by lazy { ViewModelProviders.of(this).get(TestViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendarLinear = findViewById<CalendarLinearLayout2>(R.id.calendar_linear)
        val calendar = findViewById<CalendarView2>(R.id.calendar)

        calendar.setOnChangePager(object: CalendarView2.OnChangePager{
            override fun onMonthPageChange(position: Int, entity: DateEntity, viewVisibility:Int) {
                Log.i("jv.lee", "onMonthPageChange: $position - ${entity.year}-${entity.month}")
                viewModel.getMonthData(position, entity.year, entity.month)
            }

            override fun onWeekPageChange(position: Int, entity: DateEntity, viewVisibility:Int) {
                Log.i("jv.lee", "onWeekPageChange: $position - ${entity.year}-${entity.month}")
                if (entity.dayList.isNotEmpty()) {
                    Log.i("jv.lee", "onWeekPageChange: getWeekData")
                    viewModel.getWeekData(position,entity.year,entity.month,entity.dayList[0].day)
                }
            }

            override fun onDayChange(position: Int, entity: DayEntity) {
//                tvDateDescription.text = "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                Log.i("jv.lee", "onDayChange: $position - $entity")
            }

        })
        calendar.initData()
        calendarLinear.bindEventView(calendar)

        viewModel.monthLiveData.observe(this, Observer {
            (calendar.getMonthAdapter() as MonthAdapter2).updateDayStatus(it.position, it.data)
        })

        viewModel.weekLiveData.observe(this, Observer {
            (calendar.getWeekAdapter() as WeekAdapter2).updateDayStatus(it.position,it.data)
        })

    }
}