package com.lee.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.manager.CalendarManager
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author jv.lee
 * @date 2020/11/5
 * @description
 */

abstract class WeekPageAdapter :PagerAdapter(){
    private val TAG: String = "Pager"
    private val calendarManager by lazy {
        CalendarManager(
            prevMonthCount = 12
        )
    }
    protected val data: ArrayList<DateEntity> = calendarManager.getInitWeekData()
    protected val dayListAdapterMap = HashMap<Int,DayListAdapter>()
    private var viewPager: ViewPager? = null
    private var hasLoadMore = true
    var selectRowIndex = 0
    var currentSelectIndex = 0
    var currentDay:DayEntity? = null

    private var onChangeDataListener: OnChangeDataListener? = null

    fun bindViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        this.viewPager?.adapter = this
        initStartPage(calendarManager.prevMonthCount * 5)

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            var lastPosition = 0
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state != 2) return //只处理滑动结束状态
                val position = viewPager.currentItem

                //判断当前是否为最后一条数据 加载下一页
                if (hasLoadMore && position == count - 1) loadNextData()

                //当前页面监听回调 ， 重复回弹不回调
                if (lastPosition != position) {
                    lastPosition = position
                    onChangeDataListener?.onPageChangeDate(position, data[position])
                    initDaySelectChange(data[position])
                }
            }
        })
    }

    private fun initStartPage(position: Int) {
        this.viewPager?.setCurrentItem(position, false)
        this.onChangeDataListener?.onPageChangeDate(position, data[position])
        initDaySelectChange(data[position])
    }

    private fun initDaySelectChange(monthEntity: DateEntity){
        for ((index, item) in monthEntity.dayList.withIndex()) {
            if(item.isSelected){
                currentSelectIndex = index
                onChangeDataListener?.onDayChangeDate(index,item)
                currentDay = item
            }
        }
    }

    private fun loadPrevData() {
        hasLoadMore = false
        val prevMonthData = calendarManager.getPrevWeekData()
        data.addAll(0, prevMonthData)
        notifyDataSetChanged()
        hasLoadMore = true
    }

    private fun loadNextData() {
        hasLoadMore = false
        val nextMonthData = calendarManager.getNextWeekData()
        data.addAll(nextMonthData)
        notifyDataSetChanged()
        hasLoadMore = true
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = View.inflate(container.context, R.layout.item_month, null)
        itemView.id = position
        convertItemView(itemView, data[position], position)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun convertItemView(itemView: View, entity: DateEntity, position: Int) {
        val rvContainer = itemView.findViewById<RecyclerView>(R.id.rv_container)
        rvContainer.run {
            layoutManager =
                object : StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }.apply { isAutoMeasureEnabled = true }
            adapter = dayListAdapterMap[position]?: DayListAdapter(entity.dayList).also {
                dayListAdapterMap[position] = it
            }
        }
    }

    fun setOnChangeDataListener(onChangeDataListener: OnChangeDataListener) {
        this.onChangeDataListener = onChangeDataListener
    }

    interface OnChangeDataListener {
        fun onPageChangeDate(position: Int, entity: DateEntity)
        fun onDayChangeDate(position: Int, entity: DayEntity)
    }

    /**
     * 日列表适配器
     */
    inner class DayListAdapter(val data: ArrayList<DayEntity>) :
        RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

        private var selectPosition = 0

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DayListViewHolder {
            return DayListViewHolder(LayoutInflater.from(parent.context).inflate(getItemLayout(), parent, false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: DayListViewHolder, position: Int) {
            holder.bindView(position, data[position])
        }

        fun selectItemByPosition(position: Int, entity: DayEntity) {
            currentDay = entity
            currentSelectIndex = position
            updateSelectStatus(position,entity)
            onChangeDataListener?.onDayChangeDate(position,entity)
        }

        private fun initSelectRowIndex(position: Int,entity: DayEntity){
            if (entity.isSelected) {
                this@WeekPageAdapter.selectRowIndex = position / 7
            }
        }

        private fun initSelectPosition(position: Int,entity: DayEntity) {
            if(entity.isSelected) selectPosition = position
        }

        private fun updateSelectStatus(position: Int, entity: DayEntity){
            data[selectPosition].isSelected = false
            data[position].isSelected = true
            notifyItemChanged(selectPosition)
            notifyItemChanged(position)
        }

        inner class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(position: Int, entity: DayEntity) {
                initSelectRowIndex(position,entity)
                initSelectPosition(position,entity)
                convert(itemView.context, itemView, position, entity)

                itemView.setOnClickListener {
                    if(!entity.isToMonth || currentSelectIndex == position)return@setOnClickListener

                    selectItemByPosition(position,entity)
                }
            }
        }

    }

    fun selectItem(entity: DayEntity,position: Int) {
        currentDay?:return
        val calendar = Calendar.getInstance()

        calendar.set(entity.year,entity.month,entity.day)
        val tagWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val tagMaxWeek = CalendarUtils.getMaxWeekCountByYear(entity.year,entity.month,entity.day)

        calendar.set(currentDay?.year!!,currentDay?.month!!,currentDay?.day!!)
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentMaxWeek = CalendarUtils.getMaxWeekCountByYear(currentDay?.year!!,currentDay?.month!!,currentDay?.day!!)

        val week = if (entity.year > currentDay?.year!!) {
            tagWeek + (currentMaxWeek - currentWeek)
        }else if (entity.year < currentDay?.year!!) {
            -(currentWeek + (tagMaxWeek - tagWeek))
        }else{
            tagWeek - currentWeek
        }
        val index = viewPager?.currentItem!! + week
        viewPager?.currentItem = index
        dayListAdapterMap[index]?.let {
            it.selectItemByPosition(5,it.data[5])
        }
    }

    abstract fun getItemLayout(): Int

    abstract fun convert(context: Context, itemView: View, position: Int, entity: DayEntity)
}