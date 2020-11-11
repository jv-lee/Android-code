package com.lee.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


/**
 * @author jv.lee
 * @date 2020/11/10
 * @description
 */
class DispatchViewPager(context: Context, attributeSet: AttributeSet) :ViewPager(context,attributeSet){

    private var lastX = 0
    private var lastY = 0

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val x = ev!!.rawX.toInt()
//        val y = ev.rawY.toInt()
//        var dealtX = 0
//        var dealtY = 0
//
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                dealtX = 0
//                dealtY = 0
//                // 保证子View能够接收到Action_move事件
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                dealtX += Math.abs(x - lastX)
//                dealtY += Math.abs(y - lastY)
//                // 这里是够拦截的判断依据是左右滑动
//                if (dealtX >= dealtY) {
//                    Log.i("CalendarView", "dispatchTouchEvent: 左右滑动 - 自己处理")
//                    parent.requestDisallowInterceptTouchEvent(true)
//                } else {
//                    Log.i("CalendarView", "dispatchTouchEvent: 上下滑动 - 父容器处理")
//                    parent.requestDisallowInterceptTouchEvent(false)
//                }
//                lastX = x
//                lastY = y
//            }
//            MotionEvent.ACTION_CANCEL -> {
//            }
//            MotionEvent.ACTION_UP -> {
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

}