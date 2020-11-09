package com.lee.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.lee.calendar.adapter.MonthAdapter
import com.lee.calendar.adapter.MonthPageAdapter
import com.lee.calendar.adapter.WeekAdapter
import com.lee.calendar.adapter.WeekPageAdapter
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.DateEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.utils.SizeUtil
import com.lee.calendar.viewmodel.TestViewModel


class PageMonthActivity : AppCompatActivity() {

    private val TAG = PageMonthActivity::class.java.simpleName

    private val viewModel by lazy { ViewModelProviders.of(this).get(TestViewModel::class.java) }

    private val linearContainer by lazy { findViewById<LinearLayout>(R.id.linear_container) }
    private val vpMonthContainer by lazy { findViewById<ViewPager>(R.id.vp_month_container) }
    private val vpWeekContainer by lazy { findViewById<ViewPager>(R.id.vp_week_container) }
    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }

    private val monthPagerAdapter by lazy { MonthAdapter() }
    private val weekPagerAdapter by lazy { WeekAdapter() }
    private val mAnimation = PagerAnimation()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_month)

        monthPagerAdapter.setOnChangeDataListener(object : MonthPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                if (vpMonthContainer.visibility == View.VISIBLE) {
                    tvDateDescription.text = "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                    Log.i(TAG, "onPageChangeDate: month $position ${entity.year}-${entity.month}")
                }
//                viewModel.getMonthData(position, entity.year, entity.month)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (vpMonthContainer.visibility == View.VISIBLE) {
                    weekPagerAdapter.selectItem(entity)
                    Log.i(TAG, "onDayChangeDate: month $position $entity")
                }
            }
        })
        weekPagerAdapter.setOnChangeDataListener(object:WeekPageAdapter.OnChangeDataListener{
            override fun onPageChangeDate(position: Int, entity: DateEntity) {
                if (vpWeekContainer.visibility == View.VISIBLE) {
                    Log.i(TAG, "onPageChangeDate: week $position ${entity.year}-${entity.month}")
                }
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                if (vpWeekContainer.visibility == View.VISIBLE) {
                    monthPagerAdapter.selectItem(entity)
                    Log.i(TAG, "onDayChangeDate: week $position $entity")
                }
            }

        })
        weekPagerAdapter.bindViewPager(vpWeekContainer)
        monthPagerAdapter.bindViewPager(vpMonthContainer)

        viewModel.monthLiveData.observe(this, Observer {
            monthPagerAdapter.updateDayStatus(vpMonthContainer,it.position, it.data)
        })

        addTouch()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun addTouch() {
        val minHeight = SizeUtil.dp2px(this, 52f)
        val maxHeight = SizeUtil.dp2px(this, 312f)
        vpMonthContainer.run { setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom + -maxHeight) }
        var viewHeight = vpMonthContainer.bottom - vpMonthContainer.top
        var isOpen = true
        var tempTranslate = 0F
        var isScrollTouch = false

        val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                viewHeight = vpMonthContainer.bottom - vpMonthContainer.top
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

                val rowIndex = monthPagerAdapter.getRowIndex()
                val itemView = vpMonthContainer.findViewById<View>(vpMonthContainer.currentItem)
                val itemTranslationY = -(((maxHeight - newHeight) / 5) * rowIndex).toFloat()

                if (newHeight in minHeight..maxHeight) {
                    tempTranslate = itemTranslationY
                    itemView.translationY = itemTranslationY
                    vpMonthContainer.layoutParams.height = newHeight
                    vpMonthContainer.requestLayout()
                } else if (newHeight < minHeight) {
                    itemView.translationY = -(minHeight.toFloat() * rowIndex)
                    vpMonthContainer.layoutParams.height = minHeight
                    vpMonthContainer.requestLayout()
                    switchMonthOrWeekPager(false)
                } else if (newHeight > maxHeight) {
                    itemView.translationY = 0f
                    vpMonthContainer.layoutParams.height = maxHeight
                    vpMonthContainer.requestLayout()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

        }
        val gestureDetector = GestureDetector(this, gestureDetectorListener)
        linearContainer.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && isScrollTouch && vpMonthContainer.height != minHeight && vpMonthContainer.height != maxHeight) {
                isScrollTouch = false
                val rowIndex = monthPagerAdapter.getRowIndex()
                mAnimation.setDimensions(if(isOpen)maxHeight else minHeight,vpMonthContainer.height)
                mAnimation.setTranslationDimensions(if(isOpen)0F else -(minHeight.toFloat() * rowIndex),tempTranslate,isOpen)
                mAnimation.duration = 200
                vpMonthContainer.startAnimation(mAnimation)
                return@setOnTouchListener false
            }
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
        }
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
                vpMonthContainer.layoutParams.height = targetHeight

                vpMonthContainer.findViewById<View>(vpMonthContainer.currentItem).translationY = targetTranslationY

                switchMonthOrWeekPager(isOpen)
            } else {
                val stepHeight = (heightChange * interpolatedTime).toInt()
                vpMonthContainer.layoutParams.height = currentHeight + stepHeight

                val stepTranslation = (Math.abs(translationYChange) * interpolatedTime)
                val endTranslation = if(isOpen) -(Math.abs(currentTranslationY) - stepTranslation) else -(Math.abs(currentTranslationY) + stepTranslation)
                vpMonthContainer.findViewById<View>(vpMonthContainer.currentItem).translationY = endTranslation
            }
            vpMonthContainer.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    fun switchMonthOrWeekPager(isOpen: Boolean) {
        if (isOpen) {
            vpMonthContainer.visibility = View.VISIBLE
            vpWeekContainer.visibility = View.INVISIBLE
        }else{
            vpMonthContainer.visibility = View.INVISIBLE
            vpWeekContainer.visibility = View.VISIBLE
        }
    }

}