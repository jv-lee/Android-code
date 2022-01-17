package com.lee.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import com.lee.library.R
import com.lee.library.utils.LogUtil
import kotlin.math.abs


/**
 * @author jv.lee
 * @date 2022/1/14
 * @description 可拖动Layout布局
 */
class FloatingLayout : FrameLayout {

    //事件分发记录初始化位置
    private var mStartY = 0f
    private var mStartX = 0f

    //滑动事件 touch 记录移动位置
    private var mEndX = 0
    private var mEndY = 0

    //是否处于拖拽状态
    private var isDrag = false

    //事件回调
    private var mCallback: EventCallback? = null

    //复位类型
    private var reindexType: Int = ReIndexType.MOVE

    //复位动画
    private var mAnimation: ReIndexAnimation

    //是否限制边界拖动
    private var limitBound: Boolean = false

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    ) {
        context.obtainStyledAttributes(attributes, R.styleable.FloatingLayout).run {
            reindexType = getInt(R.styleable.FloatingLayout_reindex_type, ReIndexType.MOVE)
            limitBound = getBoolean(R.styleable.FloatingLayout_limitBound, false)
            recycle()
        }
        mAnimation = ReIndexAnimation(reindexType)
    }

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
            }
            MotionEvent.ACTION_MOVE -> {
                // 获取当前手指位置
                val endY: Float = ev.y
                val endX: Float = ev.x
                val distanceX: Float = mStartX - endX
                val distanceY: Float = mStartY - endY

                //判断当前View是否可以拖动 可拖动情况下 拦截父容器事件 子view处理当前滑动事件.
                if ((abs(distanceX) > abs(distanceY) && canScrollHorizontally(distanceX.toInt())) ||
                    (abs(distanceY) > abs(distanceX)) && canScrollVertically(distanceY.toInt())
                ) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    return super.dispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //复原所有属性修改 设置为基础值
                parent.requestDisallowInterceptTouchEvent(false)
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

        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrag = false
                mStartY = event.y
                mStartX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX: Float = mStartX - x
                val distanceY: Float = mStartY - y
                //父view可滑动则当前滑动事件不处理
                if (canScrollHorizontally(distanceX.toInt()) || canScrollHorizontally(distanceY.toInt())) {
                    return true
                }
                isDrag = true
                mCallback?.onDragStart()

                //当前view拖动时拦截父容器处理事件
                parent.requestDisallowInterceptTouchEvent(true)
                //计算距离上次移动了多远
                val currX: Int = x - mEndX
                val currY: Int = y - mEndY
                //设置当前偏移量实现拖动
                setDragTranslationX(currX)
                setDragTranslationY(currY)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isDrag) {
                    isDrag = false
                    mCallback?.onDragEnd()
                } else {
                    mCallback?.onClicked()
                }
                onReIndex()
            }
        }
        mEndX = x
        mEndY = y
        return true
    }

    private fun setDragTranslationY(y: Int) {
        if (limitBound) {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val dragTopLimit = top - parentWindow.top
            val dragBottomLimit = parentWindow.bottom - bottom

            when {
                (translationY + y) <= -dragTopLimit -> {
                    translationY = -dragTopLimit.toFloat()
                }
                (translationY + y) >= dragBottomLimit -> {
                    translationY = dragBottomLimit.toFloat()
                }
                else -> {
                    this.translationY = this.translationY + y
                }
            }

        } else {
            this.translationY = this.translationY + y
        }
    }

    private fun setDragTranslationX(x: Int) {
        if (limitBound) {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val dragLeftLimit = left - parentWindow.left
            val dragRightLimit = parentWindow.right - right

            when {
                (translationX + x) <= -dragLeftLimit -> {
                    translationX = -dragLeftLimit.toFloat()
                }
                (translationX + x) >= dragRightLimit -> {
                    translationX = dragRightLimit.toFloat()
                }
                else -> {
                    this.translationX = this.translationX + x
                }
            }
        } else {
            this.translationX = this.translationX + x
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

    private inner class ReIndexAnimation(@ReIndexType private val reIndexType: Int) : Animation() {

        private var targetTranslationX = 0F
        private var targetTranslationY = 0F

        private var currentTranslationX = 0F
        private var currentTranslationY = 0F

        private var changeTranslationX = 0F
        private var changeTranslationY = 0F

        fun initValue() {
            when (reIndexType) {
                ReIndexType.REINDEX_X -> {
                    this.currentTranslationX = translationX
                    targetTranslationX = sideTargetTranslationX()
                    changeTranslationX = sideChangeTranslationX()

                    this.currentTranslationY = translationY
                    targetTranslationY = targetTranslationY()
                    changeTranslationY = changeTranslationY()
                }
                ReIndexType.REINDEX_Y -> {
                    this.currentTranslationX = translationX
                    targetTranslationX = targetTranslationX()
                    changeTranslationX = changeTranslationX()

                    this.currentTranslationY = translationY
                    targetTranslationY = sideTargetTranslationY()
                    changeTranslationY = sideChangeTranslationY()
                }
                ReIndexType.REINDEX_XY -> {
                    this.currentTranslationX = translationX
                    targetTranslationX = 0f
                    changeTranslationX = reindexTranslationValue(translationX)

                    this.currentTranslationY = translationY
                    targetTranslationY = 0f
                    changeTranslationY = reindexTranslationValue(translationY)
                }
                ReIndexType.MOVE -> {
                    this.currentTranslationX = translationX
                    targetTranslationX = targetTranslationX()
                    changeTranslationX = changeTranslationX()

                    this.currentTranslationY = translationY
                    targetTranslationY = targetTranslationY()
                    changeTranslationY = changeTranslationY()
                }
            }

            duration = 200
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime >= 1) {
                translationX = targetTranslationX
                translationY = targetTranslationY
            } else {
                val stepX = (changeTranslationX * interpolatedTime)
                val stepY = (changeTranslationY * interpolatedTime)
                translationX = currentTranslationX + stepX
                translationY = currentTranslationY + stepY
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        /**
         * X轴限制边界变化值
         */
        private fun changeTranslationX(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val leftOffset = left - parentWindow.left
            val rightOffset = parentWindow.right - right
            if (translationX >= 0) {
                if (abs(translationX) >= rightOffset) return -(abs(translationX) - rightOffset)
            } else {
                if (abs(translationX) >= leftOffset) return abs(translationX) - leftOffset
            }
            return 0F
        }

        /**
         * X轴限制边界目标值
         */
        private fun targetTranslationX(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val leftOffset = left - parentWindow.left
            val rightOffset = parentWindow.right - right
            if (translationX >= 0) {
                if (abs(translationX) >= rightOffset) return rightOffset.toFloat()
            } else {
                if (abs(translationX) >= leftOffset) return -leftOffset.toFloat()
            }
            return translationX
        }


        /**
         * Y轴限制边界变化值
         */
        private fun changeTranslationY(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val topOffset = top - parentWindow.top
            val bottomOffset = parentWindow.bottom - bottom
            if (translationY >= 0) {
                if (abs(translationY) >= bottomOffset) return -(abs(translationY) - bottomOffset)
            } else {
                if (abs(translationY) >= topOffset) return abs(translationY) - topOffset
            }
            return 0F
        }

        /**
         * Y轴限制边界目标值
         */
        private fun targetTranslationY(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val topOffset = top - parentWindow.top
            val bottomOffset = parentWindow.bottom - bottom
            if (translationY >= 0) {
                if (abs(translationY) >= bottomOffset) return bottomOffset.toFloat()
            } else {
                if (abs(translationY) >= topOffset) return -topOffset.toFloat()
            }
            return translationY
        }

        /**
         * X轴回弹复位限制边界目标值
         */
        private fun sideTargetTranslationX(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            return if (abs(translationX) + (width / 2) > (parentWindow.width / 2)) {
                val maxTranslationX = parentWindow.width - width
                val offset = maxTranslationX - abs(translationX)

                val tagOffset = if (offset <= 0) maxTranslationX.toFloat()
                else (abs(offset) + abs(currentTranslationX))

                formatTranslationValue(translationX, tagOffset)
            } else {
                0F
            }
        }

        /**
         * X轴回弹复位限制边界变化值
         */
        private fun sideChangeTranslationX(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            return if (abs(translationX) + (width / 2) > (parentWindow.width / 2)) {
                val changeOffset = (parentWindow.width - width - abs(translationX))
                formatTranslationValue(translationX, changeOffset)
            } else {
                targetTranslationX - currentTranslationX
            }
        }

        /**
         * Y轴回弹复位限制边界目标值
         */
        private fun sideTargetTranslationY(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            return if (abs(translationY) + (height / 2) > (parentWindow.height / 2)) {
                val maxTranslationY = parentWindow.height - height
                val offset = maxTranslationY - abs(translationY)

                val tagOffset = if (offset <= 0) maxTranslationY.toFloat()
                else (abs(offset) + abs(currentTranslationY))

                formatTranslationValue(translationY, tagOffset)
            } else {
                0F
            }
        }

        /**
         * Y轴回弹复位限制边界变化值
         */
        private fun sideChangeTranslationY(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            return if (abs(translationY) + (height / 2) > (parentWindow.height / 2)) {
                val changeOffset = (parentWindow.height - height - abs(translationY))
                formatTranslationValue(translationY, changeOffset)
            } else {
                targetTranslationY - currentTranslationY
            }
        }

        /**
         * 平移复位数值转化
         */
        private fun reindexTranslationValue(translation: Float): Float {
            return if (translation >= 0) -translation else abs(translation)
        }

        /**
         * 根据平移值切换上下左右移动最终计算值
         */
        private fun formatTranslationValue(translation: Float, size: Float): Float {
            return if (translation >= 0) size else -size
        }

    }

    annotation class ReIndexType {
        companion object {
            const val MOVE = 0 // 不限制复位自由摆放
            const val REINDEX_XY = 1 // x，y轴同时开启复位
            const val REINDEX_X = 2 // x轴开启复位
            const val REINDEX_Y = 3 // y轴开启复位
        }
    }

    open class EventCallback {
        open fun onClicked() {}
        open fun onDragStart() {}
        open fun onDragEnd() {}
    }

    internal data class ParentWindow(val parent: ViewGroup) {
        val left: Int = 0

        val right: Int = parent.right - parent.left

        val top: Int = 0

        val bottom: Int = parent.bottom - parent.top

        val width: Int = right

        val height: Int = bottom
    }

    fun setEventCallback(callback: EventCallback) {
        this.mCallback = callback
    }

    fun setReindexType(@ReIndexType reindexType: Int) {
        mAnimation = ReIndexAnimation(reindexType)
    }

}