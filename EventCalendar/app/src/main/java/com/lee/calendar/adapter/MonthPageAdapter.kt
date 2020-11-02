package com.lee.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.CalendarManager
import com.lee.calendar.R
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.MonthEntity
import com.lee.calendar.widget.WrappingPagerAdapter

/**
 * @author jv.lee
 * @date 2020/10/30
 * @description
 */
abstract class MonthPageAdapter : WrappingPagerAdapter() {

    private val TAG: String = "Pager"
    private val calendarManager by lazy { CalendarManager(prevCount = 12) }
    private val data: ArrayList<MonthEntity> = calendarManager.getInitMonthData()
    private var viewPager: ViewPager? = null
    private var hasLoadMore = true

    private var onChangeDataListener: OnChangeDataListener? = null

    fun bindViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        this.viewPager?.adapter = this
        initStartPage(calendarManager.prevCount)

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
                    onChangeDataListener?.onChangeDate(position, data[position])
                }
            }
        })
    }

    private fun initStartPage(position: Int) {
        this.viewPager?.setCurrentItem(position, false)
        this.onChangeDataListener?.onChangeDate(position, data[position])
    }

    private fun loadPrevData() {
        hasLoadMore = false
        val prevMonthData = calendarManager.getPrevMonthData()
        data.addAll(0, prevMonthData)
        notifyDataSetChanged()
        hasLoadMore = true
    }

    private fun loadNextData() {
        hasLoadMore = false
        val nextMonthData = calendarManager.getNextMonthData()
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

    private fun convertItemView(itemView: View, entity: MonthEntity, position: Int) {
        val rvContainer = itemView.findViewById<RecyclerView>(R.id.rv_container)
        rvContainer.run {
            layoutManager =
                object : StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }.apply { isAutoMeasureEnabled = true }
            adapter = DayListAdapter(entity.dayList)
        }
    }

    fun setOnChangeDataListener(onChangeDataListener: OnChangeDataListener) {
        this.onChangeDataListener = onChangeDataListener
    }

    interface OnChangeDataListener {
        fun onChangeDate(position: Int, entity: MonthEntity)
    }

    /**
     * 日列表适配器
     */
    inner class DayListAdapter(private val data: ArrayList<DayEntity>) :
        RecyclerView.Adapter<DayListAdapter.DayListViewHolder>() {

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

        inner class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(position: Int, entity: DayEntity) {
                convert(itemView.context, itemView, position, entity)
            }
        }

    }

    abstract fun getItemLayout(): Int

    abstract fun convert(context: Context, itemView: View, position: Int, entity: DayEntity)

}