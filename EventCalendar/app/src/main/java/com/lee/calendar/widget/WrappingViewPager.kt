package com.lee.calendar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Interpolator
import android.view.animation.Transformation
import androidx.viewpager.widget.ViewPager


/**
 * ViewPager with dynamic height support. For basic usage, replace [ViewPager] with [WrappingViewPager]
 * in your layout file, and set its height property to `wrap_content`.
 *
 *
 * You also have to make your adapter inform the [WrappingViewPager] of every page change:
 * the easiest way to achieve this is to override [android.support.v4.view.PagerAdapter.setPrimaryItem]
 * and call [WrappingViewPager.onPageChanged]. To avoid unnecessary calls, only do this
 * when the page is changed, instead of the old one being reselected. For a basic example of this,
 * see how it is implemented in the library's own [WrappingFragmentStatePagerAdapter].
 *
 * @author Santeri Elo
 * @author Abhishek V (http://stackoverflow.com/a/32410274)
 * @author Vihaan Verma (http://stackoverflow.com/a/32488566)
 * @since 14-06-2016
 */
open class WrappingViewPager : ViewPager, AnimationListener {
    private var mCurrentView: View? = null
    private val mAnimation = PagerAnimation()
    private var mAnimStarted = false
    private var mAnimDuration: Long = 100

    constructor(context: Context) : super(context) {
        mAnimation.setAnimationListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context,
        attrs
    ) {
        mAnimation.setAnimationListener(this)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!mAnimStarted && mCurrentView != null) {
            var height: Int
            mCurrentView!!.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            height = mCurrentView!!.measuredHeight
            if (height < minimumHeight) {
                height = minimumHeight
            }
            val newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            if (layoutParams.height != 0 && heightMeasureSpec != newHeight) {
                mAnimation.setDimensions(height, layoutParams.height)
                mAnimation.duration = mAnimDuration
                startAnimation(mAnimation)
                mAnimStarted = true
            } else {
                heightMeasureSpec = newHeight
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * This method should be called when the ViewPager changes to another page. For best results
     * call this method in the adapter's setPrimary
     *
     * @param currentView PagerAdapter item view
     */
    fun onPageChanged(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }

    /**
     * Custom animation to animate the change of height in the [WrappingViewPager].
     */
    private inner class PagerAnimation : Animation() {
        private var targetHeight = 0
        private var currentHeight = 0
        private var heightChange = 0

        /**
         * Set the dimensions for the animation.
         *
         * @param targetHeight  View's target height
         * @param currentHeight View's current height
         */
        fun setDimensions(targetHeight: Int, currentHeight: Int) {
            this.targetHeight = targetHeight
            this.currentHeight = currentHeight
            heightChange = targetHeight - currentHeight
        }

        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            if (interpolatedTime >= 1) {
                layoutParams.height = targetHeight
            } else {
                val stepHeight = (heightChange * interpolatedTime).toInt()
                layoutParams.height = currentHeight + stepHeight
            }
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration Duration in ms
     */
    fun setAnimationDuration(duration: Long) {
        mAnimDuration = duration
    }

    /**
     * Sets the interpolator used by the animation.
     *
     * @param interpolator [Interpolator]
     */
    fun setAnimationInterpolator(interpolator: Interpolator?) {
        mAnimation.interpolator = interpolator
    }

    override fun onAnimationStart(animation: Animation) {
        mAnimStarted = true
    }

    override fun onAnimationEnd(animation: Animation) {
        mAnimStarted = false
    }

    override fun onAnimationRepeat(animation: Animation) {}
}