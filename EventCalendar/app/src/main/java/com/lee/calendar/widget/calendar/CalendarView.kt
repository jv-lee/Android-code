package com.lee.calendar.widget.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.lee.calendar.R
import com.lee.calendar.widget.calendar.adapter.BaseCalendarPageAdapter
import com.lee.calendar.widget.calendar.adapter.MonthAdapter
import com.lee.calendar.widget.calendar.adapter.WeekAdapter
import com.lee.calendar.widget.calendar.entity.DateEntity
import com.lee.calendar.widget.calendar.entity.DayEntity
import com.lee.calendar.ex.dp2px
import com.lee.calendar.widget.calendar.core.CalendarManager
import com.lee.calendar.widget.calendar.utils.ViewPager2Utils
import com.lee.calendar.widget.calendar.render.IDayRender
import com.lee.calendar.widget.calendar.utils.CalendarUtils
import java.util.*

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description 自定义日历View
 */
class CalendarView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private val TAG = CalendarView::class.java.simpleName

    private lateinit var mWeekViewPager: ViewPager2
    private lateinit var mMonthViewPager: ViewPager2

    private lateinit var mWeekViewPagerAdapter: BaseCalendarPageAdapter
    private lateinit var mMonthViewPagerAdapter: BaseCalendarPageAdapter

    private lateinit var mMonthCalendarManager: CalendarManager
    private lateinit var mWeekCalendarManager: CalendarManager

    private val minHeight: Int
    private val maxHeight: Int
    private val weekLayoutId: Int
    private val weekLayoutHeight: Int

    private val startTime: String
    private val initPrevMonthCount: Int
    private val initNextMonthCount: Int
    private val loadMonthCount: Int

    private var isReload = false

    private var lastDay: DayEntity? = null
    private var lastWeekDate: DateEntity? = null
    private var lastMonthDate: DateEntity? = null

    private var lastWeekPosition: Int = 0
    private var lastMonthPosition: Int = 0

    private var mDayRender: IDayRender? = null
    private var mChangePager: OnChangePager? = null

    fun getWeekLayoutHeight() = weekLayoutHeight
    fun getMinHeight() = minHeight + weekLayoutHeight
    fun getMaxHeight() = maxHeight + weekLayoutHeight

    fun getMonthPagerView() = mMonthViewPager
    fun getMonthAdapter() = mMonthViewPagerAdapter
    fun getWeekPagerView() = mWeekViewPager
    fun getWeekAdapter() = mWeekViewPagerAdapter

    init {

        context.obtainStyledAttributes(attributeSet, R.styleable.CalendarView).run {
            startTime = getString(R.styleable.CalendarView_startTime) ?: ""
            initPrevMonthCount = getInt(R.styleable.CalendarView_init_prev_month_count, 3)
            initNextMonthCount = getInt(R.styleable.CalendarView_init_next_month_count, 3)
            loadMonthCount = getInt(R.styleable.CalendarView_load_month_count, 3)
            weekLayoutId =
                getResourceId(R.styleable.CalendarView_week_layout, R.layout.layout_week)
            weekLayoutHeight = getDimension(
                R.styleable.CalendarView_week_layout_height,
                context.dp2px(32)
            ).toInt()
            val itemHeight = getDimension(
                R.styleable.CalendarView_itemHeight,
                context.dp2px(52)
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
        View.inflate(context, R.layout.layout_calendar_view, this)
        mWeekViewPager = findViewById(R.id.vp_week_container)
        mMonthViewPager = findViewById(R.id.vp_month_container)
        ViewPager2Utils.closeItemAnim(mWeekViewPager)
        ViewPager2Utils.closeItemAnim(mMonthViewPager)

        mWeekViewPager.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, minHeight)
        mWeekViewPager.requestLayout()
        mMonthViewPager.layoutParams =
            FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, maxHeight)
        mMonthViewPager.requestLayout()
    }

    private fun bindViewData(dayRender: IDayRender? = null) {
        this.mDayRender = dayRender
        if (!isReload) bindAdapterListener()
        mMonthViewPagerAdapter.bindPager(mMonthViewPager, initPrevMonthCount, mDayRender)
        mWeekViewPagerAdapter.bindPager(mWeekViewPager, initPrevMonthCount, mDayRender)
        if (isReload) bindAdapterListener()
    }

    private fun bindAdapterListener() {
        mMonthViewPagerAdapter.setOnChangeDataListener(object :
            BaseCalendarPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                lastMonthPosition = position
                lastMonthDate = entity
                mChangePager?.onMonthPageChange(position, entity, mMonthViewPager.visibility)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mMonthViewPager.visibility == View.VISIBLE) {
                    lastDay = entity
                    mWeekViewPagerAdapter.synchronizeSelectItem(entity)
                    mChangePager?.onDayChange(position, entity)
                }
            }

        })

        mWeekViewPagerAdapter.setOnChangeDataListener(object :
            BaseCalendarPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                lastWeekPosition = position
                lastWeekDate = entity
                mChangePager?.onWeekPageChange(position, entity, mWeekViewPager.visibility)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mWeekViewPager.visibility == View.VISIBLE) {
                    lastDay = entity
                    mMonthViewPagerAdapter.synchronizeSelectItem(entity)
                    mChangePager?.onDayChange(position, entity)
                }
            }

        })
    }

    private fun moveToLastPage() {
        if (mWeekViewPager.visibility == View.VISIBLE) {
            lastDay?.run {
                val diff = CalendarUtils.getDiffWeekCount(Calendar.getInstance().also {
                    it.set(Calendar.YEAR, year)
                    it.set(Calendar.MONTH, month)
                    it.set(Calendar.DATE, day)
                }, CalendarUtils.setFirstDayOfMonth(Calendar.getInstance()))
                mWeekViewPager.setCurrentItem(diff + mWeekViewPager.currentItem, false)
                if (diff == 0) notifyChangeListener()
            }
        } else if (mMonthViewPager.visibility == View.VISIBLE) {
            lastDay?.run {
                val diff = CalendarUtils.getDiffMonthCount(Calendar.getInstance().also {
                    it.set(Calendar.YEAR, year)
                    it.set(Calendar.MONTH, month)
                    it.set(Calendar.DATE, day)
                }, CalendarUtils.setFirstDayOfMonth(Calendar.getInstance()))
                mMonthViewPager.setCurrentItem(diff + mMonthViewPager.currentItem, false)
                if (diff == 0) notifyChangeListener()
            }
        }
    }

    private fun notifyChangeListener() {
        lastMonthDate ?: return
        lastWeekDate ?: return
        lastDay ?: return
        mChangePager?.onMonthPageChange(lastMonthPosition, lastMonthDate!!, mMonthViewPager.visibility)
        mChangePager?.onWeekPageChange(lastWeekPosition, lastWeekDate!!, mWeekViewPager.visibility)
        mChangePager?.onDayChange(0, lastDay!!)
    }

    fun initCalendar(dayRender: IDayRender? = null) {
        mMonthCalendarManager =
            CalendarManager(true, initPrevMonthCount, initNextMonthCount, loadMonthCount)
        val monthData = mMonthCalendarManager.initDateList()

        mWeekCalendarManager =
            CalendarManager(false, initPrevMonthCount, initNextMonthCount, loadMonthCount)
        val weekData = mWeekCalendarManager.initDateList()

        mMonthViewPagerAdapter = MonthAdapter(monthData)
        mWeekViewPagerAdapter = WeekAdapter(weekData)

        bindViewData(dayRender)
    }

    fun reInitCalendar(dayRender: IDayRender? = null) {
        isReload = true
        mMonthViewPagerAdapter.unBindPager(mMonthViewPager)
        mWeekViewPagerAdapter.unBindPager(mWeekViewPager)
        initCalendar(dayRender)
        moveToLastPage()
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