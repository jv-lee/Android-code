package com.lee.library.widget.nav

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.lee.library.extensions.dp2px

/**
 * @author jv.lee
 * @date 2019/5/7
 */
class NumberDotView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mTextBounds: Rect? = null
    private var mWidth = 0
    private var mHeight = 0
    private var numberCount = 0

    private var backgroundColor = Color.parseColor("#FF665B")
    private var textColor = Color.WHITE

    internal companion object {
        const val MAX_VALUE = "999+"
    }

    private fun initPaint() {
        mTextPaint.apply {
            color = textColor
            strokeWidth = 1f
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
        }
        mBackgroundPaint.apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
    }

    private fun initTextBound() {
        val text = parseNumberStr(numberCount)
        mTextBounds = Rect()
        mTextPaint.textSize = 10f
        mTextPaint.getTextBounds(text, 0, text.length, mTextBounds)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(layoutWidth(), layoutHeight())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (drawCount()) {
            drawBackground(canvas)
            drawTextCount(canvas)
        }
    }

    private fun drawCount(): Boolean {
        return numberCount > 0
    }

    private fun drawTextCount(canvas: Canvas) {
        mTextPaint.color = textColor
        val count = parseNumberStr(numberCount)
        val bounds = Rect()
        mTextPaint.textSize = (mHeight * 0.75).toFloat()
        mTextPaint.getTextBounds(count, 0, count.length, bounds)
        val offSet = (bounds.top + bounds.bottom) / 2.toFloat()
        canvas.drawText(count, mWidth / 2.toFloat(), mHeight / 2 - offSet, mTextPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        val rectF = RectF()
        rectF.left = 0f
        rectF.right = mWidth.toFloat()
        rectF.top = 0f
        rectF.bottom = mHeight.toFloat()
        canvas.drawRoundRect(rectF, mHeight / 2.toFloat(), mHeight / 2.toFloat(), mBackgroundPaint)
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        postInvalidate()
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        postInvalidate()
    }

    fun setNumberCount(numberCount: Int) {
        this.numberCount = numberCount
        requestLayout()
        initTextBound()
        postInvalidate()
    }

    private fun parseNumberStr(number: Int): String {
        return if (number >= 999) MAX_VALUE else number.toString()
    }

    private fun layoutWidth(): Int {
        var width = 0
        width = if (numberCount < 10) {
            context.dp2px(16).toInt()
        } else {
            (context.dp2px(mTextBounds?.width() ?: 0) + context.dp2px(10)).toInt()
        }
        return width
    }

    private fun layoutHeight(): Int {
        return context.dp2px(16).toInt()
    }

    init {
        initPaint()
        initTextBound()
    }
}