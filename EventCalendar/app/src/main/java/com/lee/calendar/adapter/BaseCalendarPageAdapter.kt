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

/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
abstract class BaseCalendarPageAdapter : PagerAdapter() {
    private val TAG: String = "Pager"

    protected val calendarManager by lazy { createCalendarManager() }
    protected val data: ArrayList<DateEntity> by lazy { initListData() }

    protected val dayListAdapterMap = HashMap<Int, DayListAdapter>()
    protected var viewPager: ViewPager? = null
    private var hasLoadMore = true
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
        initStartPage(data.size - 1)

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            var lastPosition = 0
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state != 2) return //只处理滑动结束状态
                val position = viewPager.currentItem

                //判断当前是否为最后一条数据 加载下一页
//                if (hasLoadMore && position == count - 1) loadMoreData()

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
        loadMoreData()
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

    private fun loadMoreData() {
        hasLoadMore = false
        data.addAll(loadMoreListData())
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
            if (viewPager?.visibility != View.VISIBLE) return
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


    abstract fun getItemLayout(): Int

    abstract fun convert(context: Context, itemView: View, position: Int, entity: DayEntity)

    abstract fun createCalendarManager(): ICalendarData
    abstract fun initListData(): ArrayList<DateEntity>
    abstract fun loadMoreListData():ArrayList<DateEntity>
    abstract fun selectItem(entity: DayEntity)
}