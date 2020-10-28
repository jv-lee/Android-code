package com.lee.calendar.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lee.calendar.CalendarManager
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.MonthEntity

/**
 * @author jv.lee
 * @date 2020/10/28
 * @description 月列表适配器
 */
class MonthPageAdapter(private val context: Context) :
    RecyclerView.Adapter<MonthPageAdapter.MonthPageViewHolder>() {

    private val calendarManager by lazy { CalendarManager() }
    private val data: ArrayList<MonthEntity> = calendarManager.getInitMonthData()
    private val pagerSnapHelper by lazy { PagerSnapHelper() }
    private var recyclerView: RecyclerView? = null
    private var isInit = false
    private var hasLoadMore = true

    private var onChangeDataListener: OnChangeDataListener? = null

    private fun loadPrevData(){
        data.addAll(0,calendarManager.getPrevMonthData())
        notifyDataSetChanged()
        recyclerView?.scrollToPosition(6)
        hasLoadMore = true
    }

    private fun loadNextData(){
        data.addAll(calendarManager.getNextMonthData())
        notifyDataSetChanged()
        hasLoadMore = true
    }

    fun bindRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = this
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1) && hasLoadMore) {
                    hasLoadMore = false
                    loadPrevData()
                    Log.i("MonthData", "onScrolled: loadPrev")
                }
                if (!recyclerView.canScrollVertically(1) && hasLoadMore) {
                    hasLoadMore = false
                    loadNextData()
                    Log.i("MonthData", "onScrolled: loadNext")
                }
            }
        })
        recyclerView.scrollToPosition(6)
        isInit = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthPageViewHolder {
        return MonthPageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_month, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MonthPageViewHolder, position: Int) {
        holder.bindView(data[position])
        if (isInit && hasLoadMore) {
//            loadMore(position)
        }
    }

    private fun loadMore(position: Int) {
        if (isInit && position == 3) {
            hasLoadMore = false
            //防止更新过快导致 RecyclerView 还处于锁定状态 就直接更新数据
            val value = ValueAnimator.ofInt(0, 1)
            value.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    data.addAll(0, calendarManager.getPrevMonthData())
                    notifyDataSetChanged()
                    hasLoadMore = true
                    Log.i("MonthData", "加载上一页数据")
                }
            })
            value.duration = 50
            value.start()
        } else if (isInit && position == itemCount - 4) {
            hasLoadMore = false
            //防止更新过快导致 RecyclerView 还处于锁定状态 就直接更新数据
            val value = ValueAnimator.ofInt(0, 1)
            value.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    data.addAll(calendarManager.getNextMonthData())
                    notifyDataSetChanged()
                    hasLoadMore = true
                    Log.i("MonthData", "加载下一页数据")
                }
            })
            value.duration = 50
            value.start()
        }
    }

    class MonthPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rvContainer by lazy { itemView.findViewById<RecyclerView>(R.id.rv_month_container) }

        fun bindView(entity: MonthEntity) {
            rvContainer.run {
                layoutManager =
                    GridLayoutManager(itemView.context, 7).apply { isAutoMeasureEnabled = true }
                adapter = DayListAdapter(entity.dayList)
            }
        }
    }

    fun setOnChangeDataListener(onChangeDataListener: OnChangeDataListener) {
        this.onChangeDataListener = onChangeDataListener
    }

    interface OnChangeDataListener {
        fun onChangeDate(year: Int, month: Int)
        fun onChangePosition(position: Int)
    }

}

/**
 * 日列表适配器
 */
class DayListAdapter(private val data: ArrayList<DayEntity>) :
    RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayListViewHolder {
        return DayListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DayListViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDayText by lazy { itemView.findViewById<TextView>(R.id.tv_day_text) }
        fun bindView(entity: DayEntity) {
            tvDayText.text = "${entity.year}-${entity.month}-${entity.day}"
        }
    }
}
