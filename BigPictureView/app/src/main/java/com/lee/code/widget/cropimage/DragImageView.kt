package com.lee.code.widget.cropimage

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * @author jv.lee
 * @date 2020/12/16
 * @description 可拖拽的ImageView
 * 当前实现为向下拖拽进入拖拽模式 ， 横向 向上不进入拖拽模式.
 */
class DragImageView : CropImageView {

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet, defaultStyle: Int) : super(
        context,
        attributeSet,
        defaultStyle
    )

    private val TAG = DragImageView::class.java.simpleName

    //事件分发记录初始化位置
    private var mStartY = 0f
    private var mStartX = 0f

    //滑动事件 touch 记录移动位置
    private var mEndX = 0
    private var mEndY = 0

    //有效滑动距离阈值
    private var mTouchSlop = 5

    //实时移动时间戳
    private var mMoveMillis = 0L

    private var isParentTouch = false
    private var isChildTouch = false

    private val mAnimation = ReIndexAnimation()
    private var mCallback: Callback? = null

    private val mSingleTapRunnable = Runnable { mCallback?.onClicked() }

    private val mGesture =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                removeCallbacks(mSingleTapRunnable)
                return false
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                postDelayed(mSingleTapRunnable, 200)
                return false
            }
        })

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //多点触控则直接交由当前view处理 拦截父容器处理事件
        if (ev.pointerCount > 1) {
            parent.requestDisallowInterceptTouchEvent(true)
            return super.dispatchTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                mStartY = ev.y
                mStartX = ev.x
                // 初始化标记
                isParentTouch = false
                isChildTouch = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 父容器为可拖动 子View为不可拖动 直接返回false 父容器消费事件
                if (isParentTouch && !isChildTouch) {
                    return false
                }
                // 获取当前手指位置
                val endY: Float = ev.y
                val endX: Float = ev.x
                val distanceX: Float = Math.abs(endX - mStartX)
                val distanceY: Float = Math.abs(endY - mStartY)

                //判断当前View是否可以拖动 可拖动情况下 拦截父容器事件 子view处理当前滑动事件.
                if (canScrollHorizontally(0) || canScrollVertically(0)) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    return super.dispatchTouchEvent(ev)
                }
                // 当前子view不可消费事件 且为横向拖动 则返回false 子view不处理 直接返回父容器处理事件
                if (!isChildTouch && distanceX > distanceY) {
                    isParentTouch = true
                    return false
                }
                // 当前子view不可消费事件 且滑动为向上滑动 则返回false 直接返回父亲容器处理事件
                if (!isChildTouch && endY < mStartY) {
                    isParentTouch = true
                    return false
                }
                // 滑动为向下滑动 关闭父容器处理事件 打开子容器处理事件
                if (endY > mStartY) {
                    isChildTouch = true
                    isParentTouch = false
                    parent.requestDisallowInterceptTouchEvent(true)
                    return super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //复原所有属性修改 设置为基础值
                parent.requestDisallowInterceptTouchEvent(false)
                isParentTouch = false
                isChildTouch = false
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        //多点触控直接交由父view处理
        if (event.pointerCount > 1) {
            parent.requestDisallowInterceptTouchEvent(true)
            return true
        }
        //父view可滑动则当前滑动事件不处理
        if (canScrollHorizontally(0) || canScrollHorizontally(0)) {
            return true
        }
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                //当前view拖动时拦截父容器处理事件
                parent.requestDisallowInterceptTouchEvent(true)
                //计算距离上次移动了多远
                val currX: Int = x - mEndX
                val currY: Int = y - mEndY
                //设置当前偏移量实现拖动
                setDragTranslation(currX, currY)
                setDragScale(mEndY - y)
                if (currX != 0 && currY != 0) {
                    isClickable = false
                }

                //记录最后移动时间
                if (currY > mTouchSlop) {
                    mMoveMillis = System.currentTimeMillis()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isClickable = true
                //获取当前时间减最后移动时间 如果超过500毫秒 代表用户悬停 onReIndex  否则直接关闭.
                if ((System.currentTimeMillis() - mMoveMillis) < 500 && translationY > 0) {
                    mCallback?.onDragClose()
                } else {
                    onReIndex()
                }
            }
        }
        mEndX = x
        mEndY = y
        mGesture.onTouchEvent(event)
        return true
    }

    private fun setDragTranslation(x: Int, y: Int) {
        this.translationX = this.translationX + x
        this.translationY = this.translationY + y
    }

    private fun setDragScale(y: Int) {
        val scaleValue = scaleX + (y * 0.001F)
        when {
            scaleValue >= 1 -> {
                this.scaleX = 1f
                this.scaleY = 1f
                mCallback?.changeAlpha(1f)
            }
            scaleValue <= 0.5F -> {
                this.scaleX = 0.5f
                this.scaleY = 0.5f
                mCallback?.changeAlpha(0.5f)
            }
            else -> {
                this.scaleX = scaleValue
                this.scaleY = scaleValue
                mCallback?.changeAlpha(scaleValue)
            }
        }
    }

    /**
     * 位置重置
     */
    private fun onReIndex() {
        //平移回到该view水平方向的初始点
        if (scaleX == 1.0F && translationX == 0F && translationY == 0F) return
        mAnimation.initValue()
        startAnimation(mAnimation)
    }

    fun setCallback(callback: Callback) {
        this.mCallback = callback
    }

    private inner class ReIndexAnimation : Animation() {

        private var targetTranslationX = 0F
        private var targetTranslationY = 0F
        private var targetScale = 1F

        private var currentTranslationX = 0F
        private var currentTranslationY = 0F
        private var currentScaleX = 0F

        private var translationXChange = 0F
        private var translationYChange = 0F
        private var scaleChange = 0F

        fun initValue() {
            this.currentTranslationX = translationX
            this.currentTranslationY = translationY
            this.currentScaleX = scaleX

            translationXChange = targetTranslationX - currentTranslationX
            translationYChange = targetTranslationY - currentTranslationY
            scaleChange = targetScale - currentScaleX

            duration = 200
        }


        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime >= 1) {
                translationX = targetTranslationX
                translationY = targetTranslationY
                scaleX = targetScale
                scaleY = targetScale
            } else {
                val stepX = (translationXChange * interpolatedTime)
                val stepY = (translationYChange * interpolatedTime)
                val stepScale = (scaleChange * interpolatedTime)

                translationX = currentTranslationX + stepX
                translationY = currentTranslationY + stepY
                scaleX = currentScaleX + stepScale
                scaleY = currentScaleX + stepScale
            }
            mCallback?.changeAlpha(scaleX)
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    interface Callback {
        fun onClicked()
        fun onDragClose()
        fun changeAlpha(alpha: Float)
    }

}