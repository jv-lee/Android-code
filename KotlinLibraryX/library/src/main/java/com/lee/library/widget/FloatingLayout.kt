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

                //当前是否被拖动
                if (distanceX == 0F && distanceY == 0F) {
                    return false
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

    /**
     * 设置拖动平移Y值
     */
    private fun setDragTranslationY(y: Int) {
        // 是否限制拖动限制边界
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

    /**
     * 设置拖动平移X值
     */
    private fun setDragTranslationX(x: Int) {
        // 是否限制拖动限制边界
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

    /**
     * 拖拽复位动画
     * @param reIndexType 复位动画属性
     * @see ReIndexType
     */
    private inner class ReIndexAnimation(@ReIndexType private val reIndexType: Int) : Animation() {

        // 目标平移值
        private var targetTranslationX = 0F
        private var targetTranslationY = 0F

        // 当前平移值
        private var currentTranslationX = 0F
        private var currentTranslationY = 0F

        // 变化过度平移值
        private var changeTranslationX = 0F
        private var changeTranslationY = 0F

        fun initValue() {
            // 初始化复位动画属性
            this.currentTranslationX = translationX
            targetTranslationX = targetTranslationX()
            changeTranslationX = changeTranslationX()

            this.currentTranslationY = translationY
            targetTranslationY = targetTranslationY()
            changeTranslationY = changeTranslationY()

            when (reIndexType) {
                // 设置X轴吸附效果
                ReIndexType.REINDEX_X -> {
                    targetTranslationX = sideTargetTranslationX()
                    changeTranslationX = sideChangeTranslationX()
                }
                // 设置Y轴吸附效果
                ReIndexType.REINDEX_Y -> {
                    targetTranslationY = sideTargetTranslationY()
                    changeTranslationY = sideChangeTranslationY()
                }
                // 设置XY轴回弹复位
                ReIndexType.REINDEX_XY -> {
                    targetTranslationX = 0f
                    changeTranslationX = reindexTranslationValue(translationX)

                    targetTranslationY = 0f
                    changeTranslationY = reindexTranslationValue(translationY)
                }
                // 默认为move属性
                ReIndexType.MOVE -> {
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
            val viewCenterX = width / 2

            // 向右拖动起始点为左边
            if (translationX >= 0 && viewCenterX < parentWindow.centerX) {
                // 移动view中心超过父容器中心 位置移至右边吸附
                if ((translationX + viewCenterX) > parentWindow.centerX) {
                    return (parentWindow.width - right).toFloat()
                }
                // 向左拖动起始点为右
            } else if (translationX <= 0) {
                // 移动view中心超过父容器中心 位置移至左边吸附
                if ((abs(translationX) + viewCenterX) > parentWindow.centerX) {
                    return -left.toFloat()
                }
            }

            return 0F
        }

        /**
         * X轴回弹复位限制边界变化值
         */
        private fun sideChangeTranslationX(): Float {
            return targetTranslationX - currentTranslationX
        }

        /**
         * Y轴回弹复位限制边界目标值
         */
        private fun sideTargetTranslationY(): Float {
            val parentWindow = ParentWindow(parent as ViewGroup)
            val viewCenterY = height / 2

            // 向下拖动起始点为上边
            if (translationY >= 0 && viewCenterY < parentWindow.centerY) {
                // 移动view中心超过父容器中心 位置移至右边吸附
                if ((translationY + viewCenterY) > parentWindow.centerY) {
                    return (parentWindow.height - bottom).toFloat()
                }
                // 向上拖动起始点为下
            } else if (translationY <= 0) {
                // 移动view中心超过父容器中心 位置移至左边吸附
                if ((abs(translationY) + viewCenterY) > parentWindow.centerY) {
                    return -top.toFloat()
                }
            }

            return 0F
        }

        /**
         * Y轴回弹复位限制边界变化值
         */
        private fun sideChangeTranslationY(): Float {
            return targetTranslationY - currentTranslationY
        }

        /**
         * 平移复位数值转化
         */
        private fun reindexTranslationValue(translation: Float): Float {
            return if (translation >= 0) -translation else abs(translation)
        }

    }

    /**
     * 复位类型
     */
    annotation class ReIndexType {
        companion object {
            const val MOVE = 0 // 不限制复位自由摆放
            const val REINDEX_XY = 1 // x，y轴同时开启复位
            const val REINDEX_X = 2 // x轴开启复位
            const val REINDEX_Y = 3 // y轴开启复位
        }
    }

    /**
     * 事件回调
     */
    open class EventCallback {
        open fun onClicked() {}
        open fun onDragStart() {}
        open fun onDragEnd() {}
    }

    /**
     * 父容器属性
     */
    internal data class ParentWindow(val parent: ViewGroup) {
        val left: Int = 0

        val right: Int = parent.right - parent.left

        val top: Int = 0

        val bottom: Int = parent.bottom - parent.top

        val width: Int = right

        val height: Int = bottom

        val centerX: Int = width / 2

        val centerY: Int = height / 2
    }

    fun setEventCallback(callback: EventCallback) {
        this.mCallback = callback
    }

    fun setReindexType(@ReIndexType reindexType: Int) {
        mAnimation = ReIndexAnimation(reindexType)
    }

}