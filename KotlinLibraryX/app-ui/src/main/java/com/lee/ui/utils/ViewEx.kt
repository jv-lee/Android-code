package com.lee.ui.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2

/**
 * @author jv.lee
 * @data 2021/9/22
 * @description
 */
fun ViewPager2.moveToItem(
    itemIndex: Int,
    duration: Long = 500,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
) {
    val pxToDrag: Int = width * (itemIndex - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(-currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            beginFakeDrag()
        }

        override fun onAnimationEnd(animation: Animator?) {
            endFakeDrag()
        }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}