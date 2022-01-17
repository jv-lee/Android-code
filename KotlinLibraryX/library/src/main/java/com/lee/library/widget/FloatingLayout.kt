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

    //有效滑动距离阈值
    private var mTouchSlop = 5

    //实时移动时间戳
    private var mMoveMillis = 0L

    //是否处于拖拽状态
    private var isDrag = false

    //事件回调
    private var mCallback: EventCallback? = null

    //复位类型
    private var reindexType: Int = ReIndexType.REINDEX_XY

    //复位动画
    private var mAnimation: ReIndexAnimation

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    ) {
        context.obtainStyledAttributes(attributes, R.styleable.FloatingLayout).run {
            reindexType = getInt(R.styleable.FloatingLayout_reindex_type, ReIndexType.REINDEX_XY)
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
                mCallback?.onDargStart()

                //当前view拖动时拦截父容器处理事件
                parent.requestDisallowInterceptTouchEvent(true)
                //计算距离上次移动了多远
                val currX: Int = x - mEndX
                val currY: Int = y - mEndY
                //设置当前偏移量实现拖动
                setDragTranslationX(currX)
                setDragTranslationY(currY)
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
                if (isDrag) {
                    isDrag = false
                    mCallback?.onDargEnd()
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
        val parentGroup = parent as ViewGroup
        val dargTopLimit = top - parentGroup.top
        val dargBottomLimit = parentGroup.bottom - bottom

        when {
            (translationY + y) <= -dargTopLimit -> {
                translationY = -dargTopLimit.toFloat()
            }
            (translationY + y) >= dargBottomLimit -> {
                translationY = dargBottomLimit.toFloat()
            }
            else -> {
                this.translationY = this.translationY + y
            }
        }
    }

    private fun setDragTranslationX(x: Int) {
        this.translationX = this.translationX + x
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

    annotation class ReIndexType {
        companion object {
            const val REINDEX_XY = 0 // x，y轴同时开启复位
            const val REINDEX_X = 1 // x轴开启复位
            const val REINDEX_Y = 2 // y轴开启复位
            const val REINDEX_SIDE = 3 // 任意移动，松开后左右边界自动吸附
        }
    }

    private inner class ReIndexAnimation(@ReIndexType private val reIndexType: Int) : Animation() {

        private var targetTranslationX = 0F
        private var targetTranslationY = 0F

        private var currentTranslationX = 0F
        private var currentTranslationY = 0F

        private var translationXChange = 0F
        private var translationYChange = 0F

        fun initValue() {
            if (reIndexType == ReIndexType.REINDEX_SIDE) {
                this.currentTranslationY = translationY
                translationYChange = targetTranslationY - currentTranslationY

                this.currentTranslationX = translationX
                targetTranslationX = getSideTranslationTag()
                translationXChange = getSideTranslationChange()
            } else {
                this.currentTranslationX = translationX
                translationXChange = targetTranslationX - currentTranslationX

                this.currentTranslationY = translationY
                translationYChange = targetTranslationY - currentTranslationY
            }

            duration = 200
        }


        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime >= 1) {
                when (reIndexType) {
                    ReIndexType.REINDEX_XY -> {
                        translationX = targetTranslationX
                        translationY = targetTranslationY
                    }
                    ReIndexType.REINDEX_X, ReIndexType.REINDEX_SIDE -> {
                        translationX = targetTranslationX
                    }
                    ReIndexType.REINDEX_Y -> {
                        translationY = targetTranslationY
                    }
                }
            } else {
                when (reIndexType) {
                    ReIndexType.REINDEX_XY -> {
                        val stepX = (translationXChange * interpolatedTime)
                        val stepY = (translationYChange * interpolatedTime)
                        translationX = currentTranslationX + stepX
                        translationY = currentTranslationY + stepY
                    }
                    ReIndexType.REINDEX_X, ReIndexType.REINDEX_SIDE -> {
                        val stepX = (translationXChange * interpolatedTime)
                        translationX = currentTranslationX + stepX
                    }
                    ReIndexType.REINDEX_Y -> {
                        val stepY = (translationYChange * interpolatedTime)
                        translationY = currentTranslationY + stepY
                    }
                }
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

        private fun getSideTranslationTag(): Float {
            val viewGroup = parent as ViewGroup
            return if (abs(translationX) + (width / 2) > (viewGroup.width / 2)) {
                val maxTranslationX = viewGroup.width - width
                val offset = maxTranslationX - abs(translationX)
                val tagOffset = if (offset <= 0) maxTranslationX.toFloat()
                else (abs(offset) + abs(currentTranslationX))
                updateLeftRightValue(tagOffset)
            } else {
                0F
            }
        }

        private fun getSideTranslationChange(): Float {
            val viewGroup = parent as ViewGroup
            return if (abs(translationX) + (width / 2) > (viewGroup.width / 2)) {
                val changeOffset = (viewGroup.width - width - abs(translationX))
                updateLeftRightValue(changeOffset)
            } else {
                targetTranslationX - currentTranslationX
            }
        }

        private fun updateLeftRightValue(size: Float): Float {
            return if (translationX > 0) size else -size
        }

    }

    open class EventCallback {
        open fun onClicked() {}
        open fun onDargStart() {}
        open fun onDargEnd() {}
    }

    fun setEventCallback(callback: EventCallback) {
        this.mCallback = callback
    }

    fun setReindexType(@ReIndexType reindexType: Int) {
        mAnimation = ReIndexAnimation(reindexType)
    }

}