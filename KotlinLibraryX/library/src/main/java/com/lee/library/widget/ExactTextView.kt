package com.lee.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 去除TextView 本身自带的padding值
 * @author jv.lee
 * @date 2020/9/17
 */
class ExactTextView(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {

    private val mPaint: Paint = Paint()

    private val mBounds: Rect = Rect()

    override fun onDraw(canvas: Canvas) {
        val text = measureTextReactSize()
        val left = mBounds.left
        val bottom = mBounds.bottom
        mBounds.offset(-mBounds.left, -mBounds.top)
        mPaint.isAntiAlias = true
        mPaint.color = currentTextColor
        canvas.drawText(text, -left.toFloat(), (mBounds.bottom - bottom).toFloat(), mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureTextReactSize()
        setMeasuredDimension(mBounds.right - mBounds.left, -mBounds.top + mBounds.bottom)
    }

    private fun measureTextReactSize(): String {
        val text = text.toString()
        val textLength = text.length
        mPaint.textSize = textSize
        mPaint.getTextBounds(text, 0, textLength, mBounds)
        if (textLength == 0) {
            mBounds.right = mBounds.left
        }
        return text
    }

}