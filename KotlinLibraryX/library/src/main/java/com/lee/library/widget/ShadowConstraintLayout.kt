package com.lee.library.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toRect
import com.lee.library.R
import com.lee.library.extensions.dp2px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

/**
 * 阴影容器
 * @see outLineWidth
 * @see outLineColor
 * @see shadowRound
 * @see shadowBlur
 * @see shadowColor
 * @see shadowFillColor
 * @see shadowOffsetX
 * @see shadowOffsetY
 * @author jv.lee
 * @date 2020/9/16
 */
class ShadowConstraintLayout(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var mWidth = 0F
    private var mHeight = 0F
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRectF = RectF()
    private val mLineRectF = RectF()

    /**
     * 外线条宽度
     */
    private var outLineWidth: Float

    /**
     * 外线条颜色
     */
    private var outLineColor: Int

    /**
     * 容器圆角size
     */
    private var shadowRound: Float

    /**
     * 阴影范围size
     */
    private var shadowBlur: Float

    /**
     * 阴影颜色
     */
    private var shadowColor: Int

    /**
     * 内容填充颜色
     */
    private var shadowFillColor: Int

    /**
     * 阴影X轴偏移量
     */
    private var shadowOffsetX: Float

    /**
     * 阴影Y轴偏移量
     */
    private var shadowOffsetY: Float

    private var offsetLeftPadding = 0
    private var offsetTopPadding = 0
    private var offsetRightPadding = 0
    private var offsetBottomPadding = 0

    private var roundSize = 0F
    private var roundLineSize = 0F

    private var rippleEnable = false

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.ShadowConstraintLayout).run {
            outLineWidth = getDimension(R.styleable.ShadowConstraintLayout_outLineWidth, 0f)
            outLineColor =
                getColor(R.styleable.ShadowConstraintLayout_outLineColor, Color.TRANSPARENT)
            shadowRound = getDimension(R.styleable.ShadowConstraintLayout_shadowRound, 10F)
            shadowBlur = getDimension(R.styleable.ShadowConstraintLayout_shadowBlur, 10F)
            shadowColor =
                getColor(R.styleable.ShadowConstraintLayout_shadowColor, Color.TRANSPARENT)
            shadowFillColor =
                getColor(R.styleable.ShadowConstraintLayout_shadowFillColor, Color.TRANSPARENT)
            shadowOffsetX = getDimension(R.styleable.ShadowConstraintLayout_shadowOffsetX, 0F)
            shadowOffsetY = getDimension(R.styleable.ShadowConstraintLayout_shadowOffsetY, 0F)
            rippleEnable = getBoolean(R.styleable.ShadowConstraintLayout_rippleEnable, false)
            recycle()
        }
        setWillNotDraw(false)
        initPaint()
        initPaddingSize()
        initRoundSize()
        super.setPadding(
            paddingLeft + offsetLeftPadding,
            paddingTop + offsetTopPadding,
            paddingRight + offsetRightPadding,
            paddingBottom + offsetBottomPadding
        )
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        //设置前景
        if (rippleEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CoroutineScope(Dispatchers.Main).launch {
                val drawable = withContext(Dispatchers.IO) { createRippleDrawable() }
                super.setForeground(drawable)
            }
        }
    }

    /**
     * 获取实际绘制宽高 初始化padding值
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w - shadowBlur
        mHeight = h - shadowBlur
        initRectF()
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
        canvas?.drawRoundRect(mRectF, roundSize, roundSize, mPaint)
        if (outLineWidth != 0F) canvas?.drawRoundRect(
            mLineRectF,
            roundLineSize,
            roundLineSize,
            mStrokePaint
        )
    }

    private fun initRoundSize() {
        roundSize = shadowRound + outLineWidth + context.dp2px(1)
        roundLineSize = shadowRound + outLineWidth
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
            offsetTopPadding = ((shadowBlur - shadowOffsetY) + outLineWidth).toInt()
            offsetBottomPadding = ((shadowBlur + shadowOffsetY) + outLineWidth).toInt()
        } else {
            offsetTopPadding = ((shadowBlur + abs(shadowOffsetY)) + outLineWidth).toInt()
            offsetBottomPadding = ((shadowBlur - abs(shadowOffsetY)) + outLineWidth).toInt()
        }
        if (shadowOffsetX > 0) {
            offsetLeftPadding = ((shadowBlur - shadowOffsetX) + outLineWidth).toInt()
            offsetRightPadding = ((shadowBlur + shadowOffsetX) + outLineWidth).toInt()
        } else {
            offsetLeftPadding = ((shadowBlur + abs(shadowOffsetX)) + outLineWidth).toInt()
            offsetRightPadding = ((shadowBlur - abs(shadowOffsetX)) + outLineWidth).toInt()
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

    fun setShadowFillColor(color: Int) {
        shadowFillColor = color
        mPaint.color = color
        invalidate()
    }

    fun setShadowColor(color: Int) {
        shadowColor = color
        invalidate()
    }

    override fun setForeground(foreground: Drawable?) {
    }

    override fun onDrawForeground(canvas: Canvas?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val foreground = foreground
            if (foreground != null && canvas != null) {
                foreground.bounds = mRectF.toRect()
                foreground.draw(canvas)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createRippleDrawable(): Drawable {
        val stateList = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_activated),
            intArrayOf()
        )

        val attr =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorControlHighlight))
        val color = attr.getColor(0, Color.TRANSPARENT)
        attr.recycle()

        val stateColorList = intArrayOf(color, color, color, color)
        val colorStateList = ColorStateList(stateList, stateColorList)

        return RippleDrawable(colorStateList, null, getShape())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getShape(): Drawable {
        return ShapeDrawable(object : RectShape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                canvas.drawRoundRect(0f, 0f, width, height, roundSize, roundSize, paint)
            }
        })
    }

}