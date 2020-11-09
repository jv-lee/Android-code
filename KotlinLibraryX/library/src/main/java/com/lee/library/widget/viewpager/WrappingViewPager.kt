package com.lee.library.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
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
open class WrappingViewPager(context:Context,attrs: AttributeSet?,private var isVertical:Boolean =false) : ViewPager(context,attrs), AnimationListener {
    private var mLastPosition = 0
    private var mCurrentPosition = 0
    private val diffPadding = 126

    private var mCurrentView: View? = null
    private val mAnimation = PagerAnimation()
    private var mAnimStarted = false
    private var mAnimDuration: Long = 300
    private var isUpdatePadding = false

    init {
        mAnimation.setAnimationListener(this)
    }

    protected fun setVerticalEnable(enable: Boolean) {
        this.isVertical = enable
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (!mAnimStarted && mCurrentView != null) {
            var height: Int
            mCurrentView!!.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            height = mCurrentView!!.measuredHeight
            //垂直viewpager 翻页填充问题修复 添加tag
            if (layoutParams.height < height && mLastPosition > mCurrentPosition) {
                isUpdatePadding = true
            }
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
        mLastPosition = mCurrentPosition
        mCurrentPosition = currentItem
        requestLayout()
    }

    /**
     * 清除垂直viewPager itemView 留白padding
     */
    private fun clearVerticalViewEmptyPadding(){
        if (!isVertical)return
        val view = findViewById<View>(currentItem + 1)
        view?.let {view->
            val paddingTop = view.paddingTop
            if (paddingTop >= diffPadding) {
                view.setPadding(
                    view.paddingLeft,
                    paddingTop - diffPadding,
                    view.paddingRight,
                    view.paddingBottom
                )
            }
        }
    }

    /**
     * 添加垂直itemView留白padding
     */
    private fun setVerticalViewEmptyPadding(){
        if (isUpdatePadding && isVertical) {
            //TODO instantiateItem itemView.id = position
            val view = findViewById<View>(currentItem + 1)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + diffPadding,
                view.paddingRight,
                view.paddingBottom
            )
            isUpdatePadding = false
        }
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
        setVerticalViewEmptyPadding()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        clearVerticalViewEmptyPadding()
        return super.onTouchEvent(ev)
    }

    override fun onAnimationEnd(animation: Animation) {
        mAnimStarted = false

    }

    override fun onAnimationRepeat(animation: Animation) {}
}