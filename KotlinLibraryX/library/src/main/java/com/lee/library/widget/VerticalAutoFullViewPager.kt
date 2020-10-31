package com.lee.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager


/**
 * @author jv.lee
 * @date 2020/10/30
 * @description 垂直自动填充高度ViewPager 高度设置为math_parent 正常填充父容器， wrap_content 则自适应子view高度
 */
class VerticalAutoFullViewPager : ViewPager {

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

        //添加page翻页完毕后 重新测量高度
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                requestLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

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
     * 设置重新测量高度 自适应子view高度
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //TODO find the current child view  当前方法必需在adapter.instantiateItem方法中将itemView设置当前position为ID
        var view = findViewById<View>(currentItem)
        if (view == null) {
            view = getChildAt(currentItem)
        }
        view?.measure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measureHeight(heightMeasureSpec, view))
    }

    /**
     * 测量子view高度
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureHeight(measureSpec: Int, view: View?): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.measuredHeight
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }


}