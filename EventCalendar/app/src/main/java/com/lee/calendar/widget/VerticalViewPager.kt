package com.lee.calendar.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author jv.lee
 * @date 2020/11/2
 * @description
 */
class VerticalViewPager : WrappingViewPager {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        //将viewpager翻转
        setPageTransformer(true, VerticalPageTransformer())
        // 设置去掉滑到最左或最右时的滑动效果
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : PageTransformer {
        override fun transformPage(view: View, position: Float) {
            if (position < -1) { // [-Infinity,-1)
                // 当前页的上一页
                view.alpha = 0f
            } else if (position <= 1) { // [-1,1]
                view.alpha = 1f
                // 抵消默认幻灯片过渡
                view.translationX = view.width * -position
                //设置从上滑动到Y位置
                val yPosition = position * view.height
                view.translationY = yPosition
            } else { // (1,+Infinity]
                // 当前页的下一页
                view.alpha = 0f
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev)
        return intercepted //为所有子视图返回触摸的原始坐标
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

    /**
     * 交换触摸事件的X和Y坐标
     */
    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

}