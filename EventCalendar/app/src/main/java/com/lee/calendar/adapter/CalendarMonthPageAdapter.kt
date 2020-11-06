package com.lee.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.lee.calendar.manager.CalendarManager
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DateEntity


/**
 * @author jv.lee
 * @date 2020/10/28
 * @description 月列表适配器
 */
abstract class CalendarMonthPageAdapter(private val context: Context) :
    RecyclerView.Adapter<CalendarMonthPageAdapter.CalendarMonthPageViewHolder>() {

    private val calendarManager by lazy { CalendarManager() }
    private val data: ArrayList<DateEntity> = calendarManager.getInitMonthData()
    private val pagerSnapHelper by lazy { PagerSnapHelper() }
    private var recyclerView: RecyclerView? = null
    private var loadIndex = 0
    private var hasLoadMore = true

    private var onChangeDataListener: OnChangeDataListener? = null

    private fun loadPrevData() {
        val prevMonthData = calendarManager.getPrevMonthData()
        data.addAll(0, prevMonthData)
        notifyItemRangeInserted(0, prevMonthData.size)
        hasLoadMore = true
    }

    private fun loadNextData() {
        val nextMonthData = calendarManager.getNextMonthData()
        data.addAll(nextMonthData)
        notifyItemRangeInserted(data.size,6)
        hasLoadMore = true
    }

    fun getData(): ArrayList<DateEntity> {
        return data
    }

    fun bindRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = this
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.layoutManager is LinearLayoutManager) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = linearLayoutManager.findFirstVisibleItemPosition()
                    val childAt = linearLayoutManager.getChildAt(position)
                    if (childAt != null) {
                        val layoutParams = recyclerView.layoutParams
                        layoutParams.height = childAt.height
                        recyclerView.layoutParams = layoutParams
                    }
                    val entity = data[position]
                    onChangeDataListener?.onChangeDate(position, entity)

                    if (!recyclerView.canScrollVertically(-1) && hasLoadMore) {
                        hasLoadMore = false
                        loadPrevData()
                    }
                    if (!recyclerView.canScrollVertically(1) && hasLoadMore) {
                        hasLoadMore = false
                        loadNextData()
                    }
                }
            }

        })

        recyclerView.scrollToPosition(6)
        onChangeDataListener?.onChangeDate(6, data[6])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMonthPageViewHolder {
        return CalendarMonthPageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_month, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CalendarMonthPageViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    inner class CalendarMonthPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rvContainer by lazy { itemView.findViewById<RecyclerView>(R.id.rv_container) }

        fun bindView(entity: DateEntity) {
            rvContainer.run {
                layoutManager =
                    object : StaggeredGridLayoutManager(7,StaggeredGridLayoutManager.VERTICAL) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }.apply { isAutoMeasureEnabled = true }
                adapter = CalendarDayListAdapter(entity.dayList)
            }
        }
    }

    fun setOnChangeDataListener(onChangeDataListener: OnChangeDataListener) {
        this.onChangeDataListener = onChangeDataListener
    }

    interface OnChangeDataListener {
        fun onChangeDate(position: Int, entity: DateEntity)
    }

    /**
     * 日列表适配器
     */
    inner class CalendarDayListAdapter(private val data: ArrayList<DayEntity>) :
        RecyclerView.Adapter<CalendarDayListAdapter.CalendarDayListViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CalendarDayListViewHolder {
            return CalendarDayListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(getItemLayout(), parent, false)
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: CalendarDayListViewHolder, position: Int) {
            holder.bindView(position, data[position])
        }

        inner class CalendarDayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(position: Int, entity: DayEntity) {
                convert(itemView.context, itemView, position, entity)
            }
        }
    }

    abstract fun getItemLayout(): Int

    abstract fun convert(context: Context, itemView: View, position: Int, entity: DayEntity)

}
