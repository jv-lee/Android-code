package com.lee.library.widget

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2022/3/31
 * @description SlidingPaneLayout 作为RecyclerView item容器侧滑事件处理
 */
class SlidingPaneItemTouchListener(context: Context) : RecyclerView.OnItemTouchListener,
    SlidingPaneLayout.PanelSlideListener {

    private var startX = 0f
    private var startY = 0f
    private val mTouchSlop = ViewConfiguration.get(context).scaledEdgeSlop
    private var mActiveItem: SlidingPaneLayout? = null
    private var isItemSlide = false

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                startY = e.y
                // 是否当前在滑动中
                if (isItemSlide) {
                    mActiveItem?.closePane()
                    return true
                }
                // 当前点击item为开启关闭并返回关闭结果true 拦截本次事件由SlidingPaneLayout处理
                val result = if (mActiveItem?.isOpen == true) {
                    mActiveItem?.closePane() ?: false
                } else {
                    // 防止快速点击在展开时点击下一个item导致跳转直接关闭
                    mActiveItem?.closePane()
                    false
                }
                // 当前活跃项item == 当前点击项item 直接不处理拦截事件
                val currentItem = findTopChildUnder(rv, startX, startY) as? SlidingPaneLayout
                if (mActiveItem == currentItem) {
                    isItemSlide = false
                    mActiveItem?.setPanelSlideListener(null)
                    return false
                }
                // 记录当前活跃item
                mActiveItem?.setPanelSlideListener(null)
                mActiveItem = currentItem
                mActiveItem?.setPanelSlideListener(this)
                return result
            }
            MotionEvent.ACTION_MOVE -> {
                // 当前是否为横行滑动 且为slidingPaneLayout则拦截父容器事件 由SlidingPaneLayout处理侧滑事件　
                if (abs(e.x - startX) > mTouchSlop) {
                    (findTopChildUnder(rv, startX, startY) as? SlidingPaneLayout)?.run {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mActiveItem?.isOpen == true) {
                    mActiveItem?.closePane()
                }
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        isItemSlide = true
    }

    override fun onPanelOpened(panel: View) {
        isItemSlide = false
    }

    override fun onPanelClosed(panel: View) {
        isItemSlide = false
    }

    private fun findTopChildUnder(parent: ViewGroup, x: Float, y: Float): View? {
        val childCount = parent.childCount
        for (i in childCount - 1 downTo 0) {
            val child = parent.getChildAt(i)
            if (x >= child.left && x < child.right && y >= child.top && y < child.bottom) {
                return child
            }
        }
        return null
    }
}

fun RecyclerView.closeAllItems(): Boolean {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is SlidingPaneLayout) {
            if (child.isOpen) {
                child.close()
                return true
            }
        }
    }
    return false
}