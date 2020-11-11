package com.lee.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description 子容器
 */
class RecyclerViewEx : RecyclerView {
    private var startY = 0

    constructor(context: Context) : super(context) {}
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle)

//    override fun onTouchEvent(e: MotionEvent): Boolean {
//        val status = (parent as LinearLayoutEx).isOpen
//
//        val linearLayoutManager: LinearLayoutManager
//        if (layoutManager is LinearLayoutManager) {
//            linearLayoutManager = layoutManager as LinearLayoutManager
//        } else {
//            return super.onTouchEvent(e)
//        }
//
//        val currentY = e.y.toInt()
//        if (e.action == MotionEvent.ACTION_DOWN) {
//            startY = currentY
//        }
//        // true/向上滑动 / false/向下滑动
//        val scrollTop = startY > currentY
//        val dimenSize = Math.abs(startY - currentY)
//
//        Log.i("jv.lee", "onTouchEvent: scrollTop: $scrollTop")
//        Log.i("jv.lee", "onTouchEvent: currentY: $currentY - startY: $startY")
//        Log.i("jv.lee", "onTouchEvent: dimenSize: $dimenSize")
//        Log.i("jv.lee", "onTouchEvent: $height")
//
//        if (linearLayoutManager.findFirstVisibleItemPosition() == 0 && status) {
//            Log.i("jv.lee", "onTouchEvent: return false")
//            return false
//        } else if (linearLayoutManager.findFirstVisibleItemPosition() == 0 && !status) {
//            return super.onTouchEvent(e)
//        } else {
//            return super.onTouchEvent(e)
//        }
//    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        val currentY = ev.y.toInt()
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                //点击后拦截符容器事件
//                startY = currentY
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                //获取布局管理器
//                val layoutManager: LinearLayoutManager
//                if (this.layoutManager is LinearLayoutManager) {
//                    layoutManager = this.layoutManager as LinearLayoutManager
//                } else {
//                    return super.onTouchEvent(ev)
//                }
//
//                //当前子rv 可见item为第一个， 且滑动的y坐标大于最开始点击的y坐标(手指往下滑动 列表向上，已经不再子容器范围内)   打开父容器处理
//                if (layoutManager.findFirstVisibleItemPosition() == 0 && currentY > startY) {
//                    Log.i("jv.lee", "dispatchTouchEvent: 父容器处理")
//                    parent.requestDisallowInterceptTouchEvent(false)
//                    return super.onTouchEvent(ev)
//                    //当前子rv 可见item为最后一个，且滑动的y坐标小于最开始点击的y坐标(手指往上滑动 列表向下)，已经不再子容器范围内  打开父容器处理
//                } else if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1 && currentY < startY) {
//                    Log.i("jv.lee", "dispatchTouchEvent: 父容器处理")
//                    parent.requestDisallowInterceptTouchEvent(false)
//                    return super.onTouchEvent(ev)
//                }
//                //父容器不处理
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

}