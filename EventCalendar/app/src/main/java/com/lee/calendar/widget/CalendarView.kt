package com.lee.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.R
import com.lee.calendar.adapter.BaseCalendarPageAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.SizeUtil

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description 自定义日历View
 */
class CalendarView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private val TAG = CalendarView::class.java.simpleName

    private lateinit var mWeekViewPager: ViewPager
    private lateinit var mMonthViewPager: ViewPager

    private var mMonthPagerAdapter: BaseCalendarPageAdapter? = null
    private var mWeekPagerAdapter: BaseCalendarPageAdapter? = null

    private val minHeight: Int
    private val maxHeight: Int
    private val weekLayoutId: Int
    private val weekLayoutHeight:Int

    private var mChangePager: OnChangePager? = null

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.CalendarView).run {
            weekLayoutId = getResourceId(R.styleable.CalendarView_week_layout, R.layout.layout_week)
            weekLayoutHeight = getDimension(R.styleable.CalendarView_week_layout_height,SizeUtil.dp2px(context,32F).toFloat()).toInt()
            val itemHeight = getDimension(R.styleable.CalendarView_itemHeight, SizeUtil.dp2px(context, 52F).toFloat())
            minHeight = itemHeight.toInt()
            maxHeight = itemHeight.toInt() * 6
            recycle()
        }
        orientation = VERTICAL
        initView()
    }

    private fun initView() {
        View.inflate(context, weekLayoutId, this)
        View.inflate(context, R.layout.layout_calendar_view, this)
        mWeekViewPager = findViewById(R.id.vp_week_container)
        mMonthViewPager = findViewById(R.id.vp_month_container)

        mWeekViewPager.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, minHeight)
        mWeekViewPager.requestLayout()
        mMonthViewPager.layoutParams =
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, maxHeight)
        mMonthViewPager.requestLayout()
    }

    fun bindAdapter(
        weekPageAdapter: BaseCalendarPageAdapter,
        monthPageAdapter: BaseCalendarPageAdapter
    ) {
        this.mWeekPagerAdapter = weekPageAdapter
        this.mMonthPagerAdapter = monthPageAdapter
        initPager()
    }

    fun getWeekLayoutHeight() = weekLayoutHeight
    fun getMinHeight() = minHeight + weekLayoutHeight
    fun getMaxHeight() = maxHeight + weekLayoutHeight
    fun getMonthPagerView() = mMonthViewPager
    fun getWeekPagerView() = mWeekViewPager
    fun getMonthAdapter() = mMonthPagerAdapter
    fun getWeekAdapter() = mWeekPagerAdapter

    private fun initPager() {
        mMonthPagerAdapter?.setOnChangeDataListener(object :
            BaseCalendarPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                mChangePager?.onMonthPageChange(position, entity, mMonthViewPager.visibility)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mMonthViewPager.visibility == View.VISIBLE) {
                    mWeekPagerAdapter?.selectItem(entity)
                    mChangePager?.onDayChange(position, entity)
                }
            }
        })
        mWeekPagerAdapter?.setOnChangeDataListener(object :
            BaseCalendarPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                mChangePager?.onWeekPageChange(position, entity, mWeekViewPager.visibility)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mWeekViewPager.visibility == View.VISIBLE) {
                    mMonthPagerAdapter?.selectItem(entity)
                    mChangePager?.onDayChange(position, entity)
                }
            }

        })

        mWeekPagerAdapter?.bindViewPager(mWeekViewPager)
        mMonthPagerAdapter?.bindViewPager(mMonthViewPager)
    }

    fun switchMonthOrWeekPager(expansionEnable: Boolean) {
        if (expansionEnable) {
            mMonthViewPager.visibility = View.VISIBLE
            mWeekViewPager.visibility = View.INVISIBLE
        } else {
            mMonthViewPager.visibility = View.INVISIBLE
            mWeekViewPager.visibility = View.VISIBLE
        }
    }

    fun setOnChangePager(onChangePager: OnChangePager) {
        this.mChangePager = onChangePager
    }

    interface OnChangePager {
        fun onMonthPageChange(position: Int, entity: DateEntity, viewVisibility: Int)
        fun onWeekPageChange(position: Int, entity: DateEntity, viewVisibility: Int)
        fun onDayChange(position: Int, entity: DayEntity)
    }

}