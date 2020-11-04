package com.lee.behavior.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * @author jv.lee
 * @date 2020/11/4
 * @description 将view至与容器第一个view之下
 */
class ViewBottomBehavior(context: Context, attributeSet: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attributeSet) {

    private val TAG = "Behavior"

    /**
     * @param parent 父容器 CoordinatorLayout
     * @param child 目标设置Behavior的View
     * @param dependency 目标View (对于child 相较于哪一个View)
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (parent.childCount == 0) throw Exception("CoordinatorLayout childCount is 0.")
        val firstChildView = parent.getChildAt(0)
        // dependency
        return dependency == firstChildView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        child.y = dependency.height.toFloat()
        return true
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: ${ev.action} - ${ev.y} - ${ev.x}")
        return super.onTouchEvent(parent, child, ev)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.i(TAG, "onNestedScroll: ")
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        Log.i(TAG, "onStartNestedScroll: ")
        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {
        Log.i(TAG, "onStopNestedScroll: ")
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

}