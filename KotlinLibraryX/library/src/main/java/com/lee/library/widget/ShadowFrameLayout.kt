package com.lee.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.lee.library.R
import kotlin.math.abs

/**
 * @author jv.lee
 * @date 2020/9/16
 * @description 阴影容器
 * @set shadowRound 容器圆角size
 * @set shadowBlur 阴影范围size
 * @set shadowColor 阴影颜色
 * @set shadowFillColor 内容填充颜色
 * @set shadowOffsetX 阴影X轴偏移量
 * @set shadowOffsetY 阴影Y轴偏移量
 */
class ShadowFrameLayout(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private var mWidth = 0F
    private var mHeight = 0F
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRectF = RectF()
    private val mLineRectF = RectF()

    private var outLineWidth: Float
    private var outLineColor: Int
    private var shadowRound: Float
    private var shadowBlur: Float
    private var shadowColor: Int
    private var shadowFillColor: Int
    private var shadowOffsetX: Float
    private var shadowOffsetY: Float

    private var offsetLeftPadding = 0
    private var offsetTopPadding = 0
    private var offsetRightPadding = 0
    private var offsetBottomPadding = 0

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.ShadowFrameLayout).run {
            outLineWidth = getDimension(R.styleable.ShadowFrameLayout_outLineWidth, 0f)
            outLineColor = getColor(R.styleable.ShadowFrameLayout_outLineColor, Color.BLACK)
            shadowRound = getDimension(R.styleable.ShadowFrameLayout_shadowRound, 10F)
            shadowBlur = getDimension(R.styleable.ShadowFrameLayout_shadowBlur, 10F)
            shadowColor = getColor(R.styleable.ShadowFrameLayout_shadowColor, Color.BLACK)
            shadowFillColor = getColor(R.styleable.ShadowFrameLayout_shadowFillColor, Color.WHITE)
            shadowOffsetX = getDimension(R.styleable.ShadowFrameLayout_shadowOffsetX, 0F)
            shadowOffsetY = getDimension(R.styleable.ShadowFrameLayout_shadowOffsetY, 0F)

            recycle()
        }
        setWillNotDraw(false)
        initPaint()
        initPaddingSize()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = measuredWidth
        var heightSize = measuredHeight

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //非精确模式 设置阴影填充
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize += (shadowBlur.toInt() * 2)
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize += (shadowBlur.toInt() * 2)
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    /**
     * 获取实际绘制宽高 初始化padding值
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w - shadowBlur
        mHeight = h - shadowBlur
        initRectF()
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    /**
     * 根据offset 调整padding值
     */
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(
            left + offsetLeftPadding,
            top + offsetTopPadding,
            right + offsetRightPadding,
            bottom + offsetBottomPadding
        )
    }

    /**
     * 绘制阴影区域
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(
            mRectF,
            shadowRound + outLineWidth,
            shadowRound + outLineWidth,
            mPaint
        )
        if (outLineWidth != 0F) {
            canvas?.drawRoundRect(mLineRectF, shadowRound, shadowRound, mStrokePaint)
        }
    }

    /**
     * 初始化画笔参数 - 设置阴影值
     */
    private fun initPaint() {
        mPaint.color = shadowFillColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = outLineWidth
        mPaint.setShadowLayer(shadowBlur, shadowOffsetX, shadowOffsetY, shadowColor)
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint)

        mStrokePaint.color = outLineColor
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = outLineWidth
    }

    /**
     * 根据offset调整内容区域padding值
     */
    private fun initPaddingSize() {
        if (shadowOffsetY > 0) {
            offsetTopPadding = (shadowBlur - shadowOffsetY).toInt()
            offsetBottomPadding = (shadowBlur + shadowOffsetY).toInt()
        } else {
            offsetTopPadding = ((shadowBlur + abs(shadowOffsetY)).toInt())
            offsetBottomPadding = (shadowBlur - abs(shadowOffsetY)).toInt()
        }
        if (shadowOffsetX > 0) {
            offsetLeftPadding = (shadowBlur - shadowOffsetX).toInt()
            offsetRightPadding = (shadowBlur + shadowOffsetX).toInt()
        } else {
            offsetLeftPadding = (shadowBlur + abs(shadowOffsetX)).toInt()
            offsetRightPadding = (shadowBlur - abs(shadowOffsetX)).toInt()
        }
    }

    /**
     * 根据 offset 调整绘制区域
     */
    private fun initRectF() {
        if (shadowOffsetY > 0) {
            mRectF.top = if (shadowOffsetY > shadowBlur) 0F else shadowBlur - shadowOffsetY
            mRectF.bottom = mHeight - shadowOffsetY
        } else {
            mRectF.top = shadowBlur + abs(shadowOffsetY)
            mRectF.bottom = mHeight
        }
        if (shadowOffsetX > 0) {
            mRectF.left = if (shadowOffsetX > shadowBlur) 0F else shadowBlur - shadowOffsetX
            mRectF.right = mWidth - shadowOffsetX
        } else {
            mRectF.left = shadowBlur + abs(shadowOffsetX)
            mRectF.right = mWidth
        }

        val lineOffset = outLineWidth / 2
        mLineRectF.top = mRectF.top + lineOffset
        mLineRectF.bottom = mRectF.bottom - lineOffset
        mLineRectF.left = mRectF.left + lineOffset
        mLineRectF.right = mRectF.right - lineOffset
    }

}