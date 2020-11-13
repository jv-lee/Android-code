package com.lee.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.R
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.manager.ICalendarData
import com.lee.calendar.utils.CalendarUtils
import java.util.*
import kotlin.collections.HashMap

/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
abstract class BaseCalendarPageAdapter : PagerAdapter() {
    private val TAG: String = "Pager"

    protected val calendarManager by lazy { createCalendarManager() }
    protected val data: ArrayList<DateEntity> by lazy { if (isMonthMode()) calendarManager.initMonthList() else calendarManager.initWeekList() }

    protected val dayListAdapterMap = HashMap<Int, DayListAdapter>()
    protected var viewPager: ViewPager? = null
    private val rowIndexMap = HashMap<Int, Int>()

    var currentSelectIndex = 0
    var currentDay: DayEntity? = null

    private var onChangeDataListener: OnChangeDataListener? = null

    fun getRowIndex(): Int {
        return rowIndexMap[viewPager?.currentItem ?: 0] ?: 0
    }

    fun bindViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        this.viewPager?.adapter = this
        initStartPage(data.size / 2)

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                onChangeDataListener?.onPageChangeDate(position,data[position])
                initDaySelectChange(data[position])

            }

        })
    }

    private fun initStartPage(position: Int) {
        val index = getInitPosition(position)
        this.viewPager?.setCurrentItem(index, false)
        this.onChangeDataListener?.onPageChangeDate(index, data[index])
        initDaySelectChange(data[index])
//        loadMoreData()
    }

    private fun getInitPosition(position: Int): Int {
        return if (isMonthMode()) {
            position
        } else {
            val dayEntity = data[position].dayList[0]
            val weekDiffCount = CalendarUtils.getDiffWeekCount(
                Calendar.getInstance().also {
                    it.set(Calendar.DATE, 1)
                }, Calendar.getInstance().also {
                    it.set(dayEntity.year, dayEntity.month, dayEntity.day)
                })
            position + weekDiffCount
        }
    }

    private fun initDaySelectChange(monthEntity: DateEntity) {
        for ((index, item) in monthEntity.dayList.withIndex()) {
            if (item.isSelected) {
                currentSelectIndex = index
                onChangeDataListener?.onDayChangeDate(index, item)
                currentDay = item
            }
        }
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
            adapter = dayListAdapterMap[position] ?: DayListAdapter(entity.dayList, position).also {
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
    inner class DayListAdapter(val data: ArrayList<DayEntity>, private val parentPosition: Int) :
        RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

        private var selectPosition = 0

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DayListViewHolder {
            return DayListViewHolder(
                LayoutInflater.from(parent.context).inflate(getItemLayout(), parent, false)
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: DayListViewHolder, position: Int) {
            holder.bindView(position, data[position])
        }

        fun initSelectRowIndex(position: Int, entity: DayEntity) {
            if (entity.isSelected) {
                this@BaseCalendarPageAdapter.rowIndexMap[parentPosition] = position / 7
            }
        }

        private fun initSelectPosition(position: Int, entity: DayEntity) {
            if (entity.isSelected) selectPosition = position
        }

        fun selectItemByPosition(position: Int, entity: DayEntity) {
            currentDay = entity
            currentSelectIndex = position
            updateSelectStatus(position)
            onChangeDataListener?.onDayChangeDate(position, entity)
        }

        private fun updateSelectStatus(position: Int) {
            data[selectPosition].isSelected = false
            data[position].isSelected = true
            notifyItemChanged(selectPosition)
            notifyItemChanged(position)
        }

        inner class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(position: Int, entity: DayEntity) {
                initSelectPosition(position, entity)
                initSelectRowIndex(position, entity)
                convert(itemView.context, itemView, position, entity)

                itemView.setOnClickListener {
                    if (!entity.isToMonth || currentSelectIndex == position) return@setOnClickListener

                    selectItemByPosition(position, entity)
                }
            }
        }

    }

    fun selectItem(entity: DayEntity) {
        if (isMonthMode()) selectMonthItem(entity) else selectWeekItem(entity)
    }

    private fun selectMonthItem(entity: DayEntity) {
        currentDay ?: return
        viewPager ?: return

        val diffMonthCount = CalendarUtils.getDiffMonthCount(
            Calendar.getInstance().also {
                it.set(entity.year, entity.month, entity.day)
            }, Calendar.getInstance().also {
                it.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)
            })

        val currentItemIndex = viewPager?.currentItem!! + diffMonthCount
        viewPager?.setCurrentItem(currentItemIndex, true)

        dayListAdapterMap[currentItemIndex]?.let {
            for ((index, day) in it.data.withIndex()) {
                if (day.day == entity.day && day.isToMonth) {
                    it.selectItemByPosition(index, it.data[index])
                    it.initSelectRowIndex(index, day)
                }
            }
        }
    }

    private fun selectWeekItem(entity: DayEntity) {
        currentDay ?: return
        viewPager ?: return

        val diffWeekCount = CalendarUtils.getDiffWeekCount(
            Calendar.getInstance().also {
                it.set(entity.year, entity.month, entity.day)
            }, Calendar.getInstance().also {
                it.set(currentDay?.year!!, currentDay?.month!!, currentDay?.day!!)
            })

        val index = viewPager?.currentItem!! + diffWeekCount

        viewPager?.setCurrentItem(index, true)

        if (dayListAdapterMap[index] == null) {
            currentDay = entity
        }
        dayListAdapterMap[index]?.let {
            for ((index, day) in it.data.withIndex()) {
                if (day.day == entity.day) {
                    it.selectItemByPosition(index, it.data[index])
                }
            }
        }
    }

    abstract fun createCalendarManager(): ICalendarData

    abstract fun isMonthMode(): Boolean

    abstract fun getItemLayout(): Int

    abstract fun convert(context: Context, itemView: View, position: Int, entity: DayEntity)

}