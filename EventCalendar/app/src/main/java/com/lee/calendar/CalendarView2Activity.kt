package com.lee.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.manager.CalendarManager2
import com.lee.calendar.widget.MonthView
import kotlinx.android.synthetic.main.activity_calendar_view_2.*

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
class CalendarView2Activity : AppCompatActivity(R.layout.activity_calendar_view_2) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendarManager = CalendarManager2(true,10,10)
        val dateList = calendarManager.initDateList()

        val viewPagerAdapter = ViewPagerAdapter(dateList)
        vp_container.adapter = viewPagerAdapter

        vp_container.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Toast.makeText(this@CalendarView2Activity,"$position",Toast.LENGTH_LONG).show()
            }
        })
        vp_container.setCurrentItem(viewPagerAdapter.itemCount / 2,false)

    }

    class ViewPagerAdapter(private val data:ArrayList<DateEntity>) :RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            return ViewPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month_view,parent,false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            holder.bindView(data[position])
        }

        class ViewPagerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

            fun bindView(entity: DateEntity) {
                Log.i("jv.lee", "bindView: ${entity.dayList}")
                itemView.findViewById<MonthView>(R.id.month_view).bindData(entity.dayList)
            }
        }



    }

}