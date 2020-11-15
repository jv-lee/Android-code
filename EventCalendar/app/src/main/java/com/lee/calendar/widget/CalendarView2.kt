package com.lee.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.lee.calendar.R
import com.lee.calendar.adapter.BaseCalendarPageAdapter2
import com.lee.calendar.adapter.MonthAdapter2
import com.lee.calendar.adapter.WeekAdapter2
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.manager.CalendarManager2
import com.lee.calendar.utils.SizeUtil
import com.lee.calendar.utils.ViewPager2Utils

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description 自定义日历View
 */
class CalendarView2(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private val TAG = CalendarView2::class.java.simpleName

    private lateinit var mWeekViewPager: ViewPager2
    private lateinit var mMonthViewPager: ViewPager2

    private lateinit var mWeekViewPagerAdapter: BaseCalendarPageAdapter2
    private lateinit var mMonthViewPagerAdapter: BaseCalendarPageAdapter2

    private val minHeight: Int
    private val maxHeight: Int
    private val weekLayoutId: Int
    private val weekLayoutHeight: Int

    private var mChangePager: OnChangePager? = null

    fun getWeekLayoutHeight() = weekLayoutHeight
    fun getMinHeight() = minHeight + weekLayoutHeight
    fun getMaxHeight() = maxHeight + weekLayoutHeight

    fun getMonthPagerView() = mMonthViewPager
    fun getMonthAdapter() = mMonthViewPagerAdapter
    fun getWeekPagerView() = mWeekViewPager
    fun getWeekAdapter() = mWeekViewPagerAdapter

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.CalendarView2).run {
            weekLayoutId =
                getResourceId(R.styleable.CalendarView2_week_layout2, R.layout.layout_week)
            weekLayoutHeight = getDimension(
                R.styleable.CalendarView2_week_layout_height2,
                SizeUtil.dp2px(context, 32F).toFloat()
            ).toInt()
            val itemHeight = getDimension(
                R.styleable.CalendarView2_itemHeight2,
                SizeUtil.dp2px(context, 52F).toFloat()
            )
            minHeight = itemHeight.toInt()
            maxHeight = itemHeight.toInt() * 6
            recycle()
        }
        orientation = VERTICAL
        initView()
    }

    private fun initView() {
        View.inflate(context, weekLayoutId, this)
        View.inflate(context, R.layout.layout_calendar_view2, this)
        mWeekViewPager = findViewById(R.id.vp_week_container)
        mMonthViewPager = findViewById(R.id.vp_month_container)
        ViewPager2Utils.closeItemAnim(mWeekViewPager)
        ViewPager2Utils.closeItemAnim(mMonthViewPager)

        mWeekViewPager.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, minHeight)
        mWeekViewPager.requestLayout()
        mMonthViewPager.layoutParams =
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, maxHeight)
        mMonthViewPager.requestLayout()

        val monthCalendarManager = CalendarManager2(true, 10, 10)
        val monthData = monthCalendarManager.initDateList()

        val weekCalendarManager = CalendarManager2(false, 10, 10)
        val weekData = weekCalendarManager.initDateList()

        MonthAdapter2(monthData).also {
            mMonthViewPagerAdapter = it
            mMonthViewPagerAdapter.setOnChangeDataListener(object :
                BaseCalendarPageAdapter2.OnChangeDataListener {
                override fun onPageChangeDate(position: Int, entity: DateEntity) {
                    mChangePager?.onMonthPageChange(position, entity, mMonthViewPager.visibility)
                }

                override fun onDayChangeDate(position: Int, entity: DayEntity) {
                    if (mMonthViewPager.visibility == View.VISIBLE) {
                        mWeekViewPagerAdapter.synchronizeSelectItem(entity)
                        mChangePager?.onDayChange(position, entity)
                    }
                }

            })
        }
        WeekAdapter2((weekData)).also {
            mWeekViewPagerAdapter = it
            mWeekViewPagerAdapter.setOnChangeDataListener(object :
                BaseCalendarPageAdapter2.OnChangeDataListener {
                override fun onPageChangeDate(position: Int, entity: DateEntity) {
                    mChangePager?.onWeekPageChange(position, entity, mWeekViewPager.visibility)
                }

                override fun onDayChangeDate(position: Int, entity: DayEntity) {
                    if (mWeekViewPager.visibility == View.VISIBLE) {
                        mMonthViewPagerAdapter.synchronizeSelectItem(entity)
                        mChangePager?.onDayChange(position, entity)
                    }
                }

            })
        }
    }

    fun initData() {
        mMonthViewPagerAdapter.bindPager(mMonthViewPager)
        mWeekViewPagerAdapter.bindPager(mWeekViewPager)
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