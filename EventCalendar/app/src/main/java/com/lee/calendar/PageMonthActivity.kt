package com.lee.calendar

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.lee.calendar.entity.DayEntity
import com.lee.calendar.entity.MonthEntity
import com.lee.calendar.utils.CalendarUtils
import com.lee.calendar.utils.SizeUtil
import com.lee.calendar.viewmodel.TestViewModel


class PageMonthActivity : AppCompatActivity() {

    private val TAG = PageMonthActivity::class.java.simpleName

    private val viewModel by lazy { ViewModelProviders.of(this).get(TestViewModel::class.java) }

    private val linearContainer by lazy { findViewById<LinearLayout>(R.id.linear_container) }
    private val vpContainer by lazy { findViewById<ViewPager>(R.id.vp_month_container) }
    private val tvDateDescription by lazy { findViewById<TextView>(R.id.tv_date_description) }

    private val monthPagerAdapter by lazy { MonthAdapter() }
    private val mAnimation = PagerAnimation()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_month)

        monthPagerAdapter.setOnChangeDataListener(object : MonthPageAdapter.OnChangeDataListener {
            override fun onPageChangeDate(position: Int, entity: MonthEntity) {
                tvDateDescription.text = "${entity.year}-${CalendarUtils.getMonthNumber(entity.month)}"
                viewModel.getMonthData(position, entity.year, entity.month)
            }

            override fun onDayChangeDate(position: Int, entity: DayEntity) {
                Toast.makeText(
                    this@PageMonthActivity,
                    "position:$position , ${entity.year}-${entity.month}-${entity.day}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        monthPagerAdapter.bindViewPager(vpContainer)

        viewModel.monthLiveData.observe(this, Observer {
            monthPagerAdapter.updateDayStatus(it.position, it.data)
        })

        addTouch()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun addTouch() {
        val minHeight = SizeUtil.dp2px(this, 52f)
        val maxHeight = SizeUtil.dp2px(this, 312f)
        vpContainer.run { setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom + -maxHeight) }
        var viewHeight = vpContainer.bottom - vpContainer.top
        var isOpen = true
        var tempTranslate = 0F
        var isScrollTouch = false

        val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                viewHeight = vpContainer.bottom - vpContainer.top
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
                val newHeight = (viewHeight + (e2.rawY - e1.rawY)).toInt()

                val rowIndex = monthPagerAdapter.selectRowIndex
                val itemView = vpContainer.findViewById<View>(vpContainer.currentItem)
                val itemTranslationY = -(((maxHeight - newHeight) / 5) * rowIndex).toFloat()

                if (newHeight in minHeight..maxHeight) {
                    tempTranslate = itemTranslationY
                    itemView.translationY = itemTranslationY
                    vpContainer.layoutParams.height = newHeight
                    vpContainer.requestLayout()
                } else if (newHeight < minHeight) {
                    itemView.translationY = -(minHeight.toFloat() * rowIndex)
                    vpContainer.layoutParams.height = minHeight
                    vpContainer.requestLayout()
                } else if (newHeight > maxHeight) {
                    itemView.translationY = 0f
                    vpContainer.layoutParams.height = maxHeight
                    vpContainer.requestLayout()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

        }
        val gestureDetector = GestureDetector(this, gestureDetectorListener)
        linearContainer.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && isScrollTouch) {
                isScrollTouch = false
                val rowIndex = monthPagerAdapter.selectRowIndex
                mAnimation.setDimensions(if(isOpen)maxHeight else minHeight,vpContainer.height)
                mAnimation.setTranslationDimensions(if(isOpen)0F else -(minHeight.toFloat() * rowIndex),tempTranslate,isOpen)
                mAnimation.duration = 200
                vpContainer.startAnimation(mAnimation)
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
                vpContainer.layoutParams.height = targetHeight

                vpContainer.findViewById<View>(vpContainer.currentItem).translationY = targetTranslationY
            } else {
                val stepHeight = (heightChange * interpolatedTime).toInt()
                vpContainer.layoutParams.height = currentHeight + stepHeight

                val stepTranslation = (Math.abs(translationYChange) * interpolatedTime)
                val endTranslation = if(isOpen) -(Math.abs(currentTranslationY) - stepTranslation) else -(Math.abs(currentTranslationY) + stepTranslation)
                vpContainer.findViewById<View>(vpContainer.currentItem).translationY = endTranslation
            }
            vpContainer.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

}