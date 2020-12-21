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

/**
 * @author jv.lee
 * @date 2020/12/21
 * @description
 */
class ZoomImageView : AppCompatImageView, ViewTreeObserver.OnGlobalLayoutListener {

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

    //单击
    private var onClickListener: OnClickListener? = null

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    init {
        scaleType = ScaleType.MATRIX

        //缩放矩阵
        mScaleMatrix = Matrix()

        //模拟滑动惯性
        scroller = OverScroller(context)

        //手势缩放
        mScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                return super.onScale(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                super.onScaleEnd(detector)
            }
        })

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                //双击监听
                e?.run { onDoubleScale(x, y) }
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                //单击事件
                onClickListener?.onClick(this@ZoomImageView)
                return true
            }
        })
    }

    override fun setOnClickListener(onClickListener: OnClickListener) {
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
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mScaleGestureDetector.onTouchEvent(event) or
                gestureDetector.onTouchEvent(event)
    }

    override fun onGlobalLayout() {
        if (mIsFirstLoad) {
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
            mIsFirstLoad = false
        }
    }


    /**
     * 双击改变大小
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

    //返回双击后改变的大小比例(我们希望缩放误差在deviation范围内)
    private fun getDoubleScale(): Float {
        val deviation = 0.05f
        var drowScale = 1.0f
        var scale: Float = getScale()
        if (Math.abs(mScale - scale) < deviation) scale = mScale
        if (Math.abs(mMidScale - scale) < deviation) scale = mMidScale
        if (scale != mScale) {
            //当前大小不等于mMidScale,则调整到mMidScale
            drowScale = mScale
        } else {
            drowScale = mMidScale
        }
        return drowScale
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
                Log.i(TAG, "scaleAnimation: ${animation.currentPlayTime}")
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

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    private fun getScale(): Float {
        val values = FloatArray(9)
        mScaleMatrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    private fun getTranslateX(): Float {
        val rectF: RectF = getMatrixRectF() ?: return 0F
        val value =  if (rectF.left > 0) {
            if (rectF.width() > width) {
                //图片宽度大于控件宽度，移动到左边贴边
                val value = -rectF.left
                value
            } else {
                //图片宽度小于控件宽度，移动到中间
                val value = width / 2f - (rectF.width() / 2f + rectF.left)
                value
            }
        } else if (rectF.right < width) {
            if (rectF.width() > width) {
                //图片宽度大于控件宽度，移动到右边贴边
                val value = width - rectF.right
                value
            } else {
                //图片宽度小于控件宽度，移动到中间
                val value = width / 2f - (rectF.width() / 2f + rectF.left)
                value
            }
        } else {
            0F
        }
        Log.i(TAG, "getTranslateX: $value")
        return value
    }

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
                rectF.height() - bottom
            } else {
                //图片高度小于控件宽度，移动到中间
                height / 2f - (rectF.height()/ 2f + rectF.top)
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

}