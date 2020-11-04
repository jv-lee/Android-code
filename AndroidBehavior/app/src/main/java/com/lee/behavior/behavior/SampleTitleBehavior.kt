package com.lee.behavior.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView

/**
 * @author jv.lee
 * @date 2020/11/4
 * @description
 */
class SampleTitleBehavior(context:Context,attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context,attrs) {
    // 列表顶部和title底部重合时，列表的滑动距离。
    private var deltaY = 0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (deltaY == 0f) {
            deltaY = dependency.y - child.height
        }
        var dy= dependency.y - child.height
        dy = (if (dy < 0F) 0F else dy)
        val y: Float = -(dy / deltaY) * child.height
        child.translationY = y
        return true
    }

}