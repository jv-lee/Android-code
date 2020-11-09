package com.lee.calendar.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.R
import com.lee.calendar.adapter.MonthPageAdapter
import com.lee.calendar.adapter.WeekPageAdapter
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.utils.SizeUtil

/**
 * @author jv.lee
 * @date 2020/11/9
 * @description
 */
class CalendarView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private val TAG = CalendarView::class.java.simpleName

    private lateinit var mWeekViewPager:ViewPager
    private lateinit var mMonthViewPager:ViewPager

    private var mMonthPagerAdapter:MonthPageAdapter? = null
    private var mWeekPagerAdapter:WeekPageAdapter?= null

    private val mAnimation = PagerAnimation()
    private val minHeight:Int
    private val maxHeight:Int
    private val weekLayoutId:Int

    private var mChangePager:OnChangePager?= null

    init {
        context.obtainStyledAttributes(attributeSet,R.styleable.CalendarView).run {
            weekLayoutId = getResourceId(R.styleable.CalendarView_week_layout,R.layout.layout_week)
            val itemHeight = getDimension(R.styleable.CalendarView_itemHeight,SizeUtil.dp2px(context,52F).toFloat())
            minHeight = itemHeight.toInt()
            maxHeight = itemHeight.toInt() * 6
            recycle()
        }
        orientation = VERTICAL
        initView()
    }

    private fun initView() {
        View.inflate(context,weekLayoutId,this)
        View.inflate(context,R.layout.layout_calendar_view,this)
        mWeekViewPager = findViewById(R.id.vp_week_container)
        mMonthViewPager = findViewById(R.id.vp_month_container)

        mWeekViewPager.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,minHeight)
        mWeekViewPager.requestLayout()
        mMonthViewPager.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,maxHeight)
        mMonthViewPager.requestLayout()
    }

    fun bindAdapter(weekPageAdapter: WeekPageAdapter, monthPageAdapter: MonthPageAdapter) {
        this.mWeekPagerAdapter = weekPageAdapter
        this.mMonthPagerAdapter = monthPageAdapter
        initPager()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addContainerTouch(viewGroup: ViewGroup) {
        mMonthViewPager.run { setPadding(paddingLeft,paddingTop,paddingRight, (paddingBottom + -maxHeight)) }
        var viewHeight = mMonthViewPager.bottom - mMonthViewPager.top
        var isOpen = true
        var tempTranslate = 0F
        var isScrollTouch = false

        val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                viewHeight = mMonthViewPager.bottom - mMonthViewPager.top
                return true
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                isScrollTouch = true
                isOpen = e2.rawY > e1.rawY

                switchMonthOrWeekPager(true)

                val newHeight = (viewHeight + (e2.rawY - e1.rawY)).toInt()

                val rowIndex = mMonthPagerAdapter?.getRowIndex()?:0
                val itemView = mMonthViewPager.findViewById<View>(mMonthViewPager.currentItem)
                val itemTranslationY = -(((maxHeight - newHeight) / 5) * rowIndex).toFloat()

                if (newHeight in minHeight..maxHeight) {
                    tempTranslate = itemTranslationY
                    itemView.translationY = itemTranslationY
                    mMonthViewPager.layoutParams.height = newHeight
                    mMonthViewPager.requestLayout()
                } else if (newHeight < minHeight) {
                    itemView.translationY = -(minHeight.toFloat() * rowIndex)
                    mMonthViewPager.layoutParams.height = minHeight.toInt()
                    mMonthViewPager.requestLayout()
                    switchMonthOrWeekPager(false)
                } else if (newHeight > maxHeight) {
                    itemView.translationY = 0f
                    mMonthViewPager.layoutParams.height = maxHeight.toInt()
                    mMonthViewPager.requestLayout()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

        }
        val gestureDetector = GestureDetector(context, gestureDetectorListener)
        viewGroup.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && isScrollTouch && mMonthViewPager.height != minHeight && mMonthViewPager.height != maxHeight) {
                isScrollTouch = false
                val rowIndex = mMonthPagerAdapter?.getRowIndex()?:0
                mAnimation.setDimensions(if(isOpen)maxHeight else minHeight,mMonthViewPager.height)
                mAnimation.setTranslationDimensions(if(isOpen)0F else -(minHeight.toFloat() * rowIndex),tempTranslate,isOpen)
                mAnimation.duration = 200
                mMonthViewPager.startAnimation(mAnimation)
                return@setOnTouchListener false
            }
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
        }
    }

    private fun initPager(){
        mMonthPagerAdapter?.setOnChangeDataListener(object : MonthPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                if (mMonthViewPager.visibility == View.VISIBLE) {
                    mChangePager?.onMonthPageChange(position, entity)
                }
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mMonthViewPager.visibility == View.VISIBLE) {
                    mWeekPagerAdapter?.selectItem(entity)
                    mChangePager?.onDayChange(position,entity)
                }
            }
        })
        mWeekPagerAdapter?.setOnChangeDataListener(object: WeekPageAdapter.OnChangeDataListener{
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                if (mWeekViewPager.visibility == View.VISIBLE) {
                    mChangePager?.onWeekPageChange(position, entity)
                }
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (mWeekViewPager.visibility == View.VISIBLE) {
                    mMonthPagerAdapter?.selectItem(entity)
                    mChangePager?.onDayChange(position,entity)
                }
            }

        })

        mWeekPagerAdapter?.bindViewPager(mWeekViewPager)
        mMonthPagerAdapter?.bindViewPager(mMonthViewPager)
    }

    private fun switchMonthOrWeekPager(isOpen: Boolean) {
        if (isOpen) {
            mMonthViewPager.visibility = View.VISIBLE
            mWeekViewPager.visibility = View.INVISIBLE
        }else{
            mMonthViewPager.visibility = View.INVISIBLE
            mWeekViewPager.visibility = View.VISIBLE
        }
    }

    fun setOnChangePager(onChangePager: OnChangePager) {
        this.mChangePager = onChangePager
    }

    interface OnChangePager {
        fun onMonthPageChange(position: Int,entity: DateEntity)
        fun onWeekPageChange(position: Int,entity: DateEntity)
        fun onDayChange(position:Int,entity:DayEntity)
    }

    private inner class PagerAnimation : Animation() {
        private var targetHeight = 0
        private var currentHeight = 0
        private var heightChange = 0

        fun setDimensions(targetHeight: Int, currentHeight: Int) {
            this.targetHeight = targetHeight
            this.currentHeight = currentHeight
            heightChange = targetHeight - currentHeight
        }

        private var targetTranslationY = 0F
        private var currentTranslationY = 0F
        private var translationYChange = 0F
        private var isOpen = false

        fun setTranslationDimensions(targetTranslation:Float,currentTranslation:Float,isOpen:Boolean){
            this.targetTranslationY = targetTranslation
            this.currentTranslationY = currentTranslation
            this.isOpen = isOpen
            translationYChange = targetTranslationY - currentTranslationY
        }

        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            if (interpolatedTime >= 1) {
                mMonthViewPager.layoutParams.height = targetHeight

                mMonthViewPager.findViewById<View>(mMonthViewPager.currentItem).translationY = targetTranslationY

                switchMonthOrWeekPager(isOpen)
            } else {
                val stepHeight = (heightChange * interpolatedTime).toInt()
                mMonthViewPager.layoutParams.height = currentHeight + stepHeight

                val stepTranslation = (Math.abs(translationYChange) * interpolatedTime)
                val endTranslation = if(isOpen) -(Math.abs(currentTranslationY) - stepTranslation) else -(Math.abs(currentTranslationY) + stepTranslation)
                mMonthViewPager.findViewById<View>(mMonthViewPager.currentItem).translationY = endTranslation
            }
            mMonthViewPager.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

}