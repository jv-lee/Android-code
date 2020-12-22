package com.lee.code.zoom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.OverScroller
import kotlin.math.roundToInt

/**
 * @author jv.lee
 * @date 2020/12/21
 * @description
 */
open class ZoomImageView : AppCompatImageView, ViewTreeObserver.OnGlobalLayoutListener {

    private val TAG = ZoomImageView::class.java.simpleName

    //view初次构建调整视图 单次调整
    private var mIsFirstLoad = true

    //初始化的比例,也就是最小比例
    private var mScale = 0f

    //图片最大比例
    private var mMaxScale = 0f

    //双击能达到的最大比例
    private var mMidScale = 0f

    //缩放矩阵
    private var mScaleMatrix: Matrix

    //捕获用户多点触控
    private var mScaleGestureDetector: ScaleGestureDetector

    //移动
    private var gestureDetector: GestureDetector

    //双击
    private var mScaleAnimator: ValueAnimator? = null

    //滚动
    private var scroller: OverScroller
    private var mCurrentX = 0
    private var mCurrentY = 0
    private var mTranslateAnimator: ValueAnimator? = null

    //单击
    private var onClickListener: OnClickListener? = null

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attributeSet,
            defStyleAttr
    )

    init {
        //设置视图类型为矩阵渲染.
        scaleType = ScaleType.MATRIX

        //缩放矩阵
        mScaleMatrix = Matrix()

        //模拟滑动惯性
        scroller = OverScroller(context)

        //手势缩放事件监听
        mScaleGestureDetector = ScaleGestureDetector(
                context,
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector?): Boolean {
                        onGestureScale(detector)
                        return true
                    }

                    override fun onScaleEnd(detector: ScaleGestureDetector?) {
                        onGestureScaleEnd(detector)
                    }
                })

        //基础手势事件监听
        gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onScroll(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            distanceX: Float,
                            distanceY: Float
                    ): Boolean {
                        onTranslationImage(-distanceX, -distanceY)
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        e?.run { onDoubleScale(x, y) }
                        return true
                    }

                    override fun onFling(
                            e1: MotionEvent,
                            e2: MotionEvent,
                            velocityX: Float,
                            velocityY: Float
                    ): Boolean {
                        return if (onFlingEvent(e2.x, e2.y, velocityX, velocityY)) {
                            super.onFling(e1, e2, velocityX, velocityY)
                        } else {
                            false
                        }
                    }

                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onClickListener?.onClick(this@ZoomImageView)
                        return true
                    }
                })
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mScaleGestureDetector.onTouchEvent(event) or
                gestureDetector.onTouchEvent(event)
    }

    override fun onGlobalLayout() {
        if (mIsFirstLoad) {
            postDelayed({ drawViewLayout() }, 300)
            mIsFirstLoad = false
        }
    }

    /**
     * 初始化调正 view视图渲染位置大小.
     */
    fun drawViewLayout() {
        Log.i(TAG, "drawViewLayout: $drawable")
        //获取图片drawable 无图片资源直接返回
        drawable ?: return

        //获取图片宽高
        val dw = drawable.intrinsicWidth
        val dh = drawable.intrinsicHeight

        var scale = 1.0f

        //计算缩放比例将图片宽度设置到目标宽高
        if (dw > width && dh <= height)
            scale = width.toFloat() / dw

        if (dw <= width && dh > height)
            scale = height.toFloat() / dh

        if ((dw <= width && dh <= height) || (dw >= width && dh >= height))
            scale = Math.min(width.toFloat() / dw, height.toFloat() / dh)

        //设置基础缩放值
        //图片原始比例
        mScale = scale
        //图片双击缩放值
        mMidScale = scale * 2
        //图片最大拉升缩放值
        mMaxScale = scale * 4

        //图片定位剧中
        val translationX = width.toFloat() / 2 - dw / 2
        val translationY = height.toFloat() / 2 - dh / 2
        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2

        mScaleMatrix.postTranslate(translationX, translationY)
        mScaleMatrix.postScale(mScale, mScale, centerX, centerY)
        imageMatrix = mScaleMatrix
    }

    /**
     * TODO Event - DoubleClick
     * 双击缩放
     *
     * @param x 点击的中心点
     * @param y 点击的中心点
     */
    private fun onDoubleScale(x: Float, y: Float) {
        //如果缩放动画已经在执行，那就不执行任何事件
        mScaleAnimator?.takeIf { it.isRunning }?.run { return }

        //执行动画缩放
        scaleAnimation(getDoubleScale(), x, y)
    }

    /**
     * todo Event - GestureScale
     * 手势缩放
     *
     * @param detector 手势操作
     */
    fun onGestureScale(detector: ScaleGestureDetector?) {
        detector ?: return
        //获取手势操作的值,scaleFactor>1说明放大，<1则说明缩小
        val scaleFactor = detector.scaleFactor
        //获取手势操作后的比例，当放操作后比例在[mInitScale,mMaxScale]区间时允许放大
        mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
        imageMatrix = mScaleMatrix
    }

    /**
     * todo Event - GestureScaleEnd
     * 手势缩放结后定位
     *
     * @param detector 手势操作
     */
    fun onGestureScaleEnd(detector: ScaleGestureDetector?) {
        detector ?: return
        val scale = detector.scaleFactor * getScale()
        if (scale < mScale) {
            scaleAnimation(mScale, width.toFloat() / 2, height.toFloat() / 2)
        } else if (scale > mMaxScale) {
            scaleAnimation(mMaxScale, width.toFloat() / 2, height.toFloat() / 2)
        }
    }

    /**
     * todo Event - TranslationImage
     * 手势移动Image
     *
     * @param dx 移动X坐标
     * @param dy 移动Y坐标
     */
    private fun onTranslationImage(dx: Float, dy: Float, isTouch: Boolean = true) {
        var dx = dx
        var dy = dy
        val rect = getMatrixRectF()
        rect ?: return

        //判断是否拖动到边界 将事件交由父容器处理
        if (isTouch && rect.right == width.toFloat() && dx < 0) {
            parent.requestDisallowInterceptTouchEvent(false)
            return
        }

        //判断是否拖动到边界 将事件交由父容器处理
        if (isTouch && rect.left == 0f && dx > 0) {
            parent.requestDisallowInterceptTouchEvent(false)
            return
        }

        //图片宽度小于控件宽度时不允许左右移动
        if (rect.width() <= width) dx = 0.0f
        //图片高度小于控件宽度时，不允许上下移动
        if (rect.height() <= height) dy = 0.0f
        //移动距离等于0，那就不需要移动了
        if (dx == 0.0f && dy == 0.0f) return
        //设置移动位置
        mScaleMatrix.postTranslate(dx, dy)
        imageMatrix = mScaleMatrix
        //调整限制位置
        mScaleMatrix.postTranslate(getTranslateX(), getTranslateY())
        imageMatrix = mScaleMatrix
    }

    /**
     * todo Event - FlingEvent
     * 滑动惯性平移事件
     *
     * @param x
     * @param y
     * @param velocityX
     * @param velocityY
     */
    private fun onFlingEvent(x: Float, y: Float, velocityX: Float, velocityY: Float): Boolean {
        mCurrentX = x.toInt()
        mCurrentY = y.toInt()

        val rect = getMatrixRectF()
        rect ?: return false

        val startX = mCurrentX
        val startY = mCurrentY

        val minX = 100
        val minY = 100
        val maxX = rect.width().roundToInt()
        val maxY = rect.height().roundToInt()
        val vX = velocityX.roundToInt()
        val vY = velocityY.roundToInt()

        if (startX != maxX || startY != maxY) {
            //模拟滑动
            scroller.fling(startX, startY, vX, vY, minX, maxX, minY, maxY, maxX, maxY)
        }

        //动画启动结束后设置为end状态
        mTranslateAnimator?.takeIf { it.isStarted }?.end()
        mTranslateAnimator = ValueAnimator.ofInt(0, 1).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 2000
            addUpdateListener {
                if (scroller.computeScrollOffset()) {
                    //获得当前的x坐标
                    val newX = scroller.currX
                    val dx = newX - mCurrentX
                    mCurrentX = newX
                    //获得当前的y坐标
                    val newY = scroller.currY
                    val dy = newY - mCurrentY
                    mCurrentY = newY
                    //进行平移操作
                    if (dx != 0 && dy != 0) onTranslationImage(dx.toFloat(), dy.toFloat(), false)
                }
            }
            start()
        }
        return true
    }

    /**
     * 缩放动画
     *
     * @param tagScale  目标缩放的比例
     * @param x         中心点
     * @param y         中心点
     */
    private fun scaleAnimation(tagScale: Float, x: Float, y: Float) {
        //如果缩放动画已经在执行，那就不执行任何事件
        mScaleAnimator?.takeIf { it.isRunning }?.run { return }

        mScaleAnimator = ObjectAnimator.ofFloat(getScale(), tagScale).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val tranScale = animation.currentPlayTime.toFloat() / animation.duration.toFloat()
                val translateX = getTranslateX() * tranScale
                val translateY = getTranslateY() * tranScale
                mScaleMatrix.postTranslate(translateX, translateY)
                val value: Float = animation.animatedValue as Float / getScale()
                mScaleMatrix.postScale(value, value, x, y)
                imageMatrix = mScaleMatrix
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    mScaleMatrix.postTranslate(getTranslateX(), getTranslateY())
                    imageMatrix = mScaleMatrix
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            start()
        }
    }

    //返回双击后改变的大小比例(我们希望缩放误差在deviation范围内)
    private fun getDoubleScale(): Float {
        val deviation = 0.05f
        var clickScale = 1.0f
        var scale: Float = getScale()
        if (Math.abs(mScale - scale) < deviation) scale = mScale
        if (Math.abs(mMidScale - scale) < deviation) scale = mMidScale
        if (scale != mScale) {
            //当前大小不等于mMidScale,则调整到mMidScale
            clickScale = mScale
        } else {
            clickScale = mMidScale
        }
        return clickScale
    }

    //获取当前图片的缩放值
    private fun getScale(): Float {
        val values = FloatArray(9)
        mScaleMatrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    //获取当前剧中translateX值
    private fun getTranslateX(): Float {
        val rectF: RectF = getMatrixRectF() ?: return 0F
        val value = if (rectF.left > 0) {
            if (rectF.width() > width) {
                //图片宽度大于控件宽度，移动到左边贴边
                -rectF.left
            } else {
                //图片宽度小于控件宽度，移动到中间
                width * 1f / 2f - (rectF.width() * 1F / 2f + rectF.left)
            }
        } else if (rectF.right < width) {
            if (rectF.width() > width) {
                //图片宽度大于控件宽度，移动到右边贴边
                width * 1F - rectF.right
            } else {
                //图片宽度小于控件宽度，移动到中间
                width * 1F / 2f - (rectF.width() / 2f + rectF.left)
            }
        } else {
            0F
        }
        return value
    }

    //获取当前剧中translateY值
    private fun getTranslateY(): Float {
        val rectF: RectF = getMatrixRectF() ?: return 0F
        return if (rectF.top > 0) {
            if (rectF.height() > height) {
                //图片高度大于控件高度，去除顶部边界
                -rectF.top
            } else {
                //图片高度小于控件宽度，移动到中间
                height / 2f - (rectF.height() / 2f + rectF.top)
            }
        } else if (rectF.bottom < height) {
            if (rectF.height() > height) {
                //图片高度大于控件高度，去除顶部边界
                height - rectF.bottom
            } else {
                //图片高度小于控件宽度，移动到中间
                height / 2f - (rectF.height() / 2f + rectF.top)
            }
        } else {
            0F
        }
    }


    //获取图片宽高以及左右上下边界
    private fun getMatrixRectF(): RectF? {
        val drawable = drawable ?: return null
        val rectF = RectF(0f, 0f, drawable.minimumWidth.toFloat(), drawable.minimumHeight.toFloat())
        val matrix = imageMatrix
        matrix.mapRect(rectF)
        return rectF
    }

    /**
     * 解决和父控件滑动冲突 只要图片边界超过控件边界，返回true
     *
     * @param direction
     * @return true 禁止父控件滑动
     */
    override fun canScrollHorizontally(direction: Int): Boolean {
        val rect = getMatrixRectF()
        rect ?: return false
        return rect.right >= width + 1 || rect.left <= -1
    }

    override fun canScrollVertically(direction: Int): Boolean {
        val rect = getMatrixRectF()
        rect ?: return false
        return rect.bottom >= height + 1 || rect.top <= -1
    }
}