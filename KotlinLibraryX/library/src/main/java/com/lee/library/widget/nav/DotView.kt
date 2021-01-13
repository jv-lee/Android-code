package com.lee.library.widget.nav

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2019/5/7
 */
class DotView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mBackgroundPaint: Paint? = null
    private var mWidth = 0
    private var mHeight = 0
    private var backgroundColor = Color.RED

    private fun initPaint() {
        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = backgroundColor
            it.strokeWidth = 1f
            it.style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG)
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_indicator_dot)
        if (drawable != null) {
            background = drawable
        } else {
            canvas.drawCircle(
                mWidth / 2.toFloat(),
                mHeight / 2.toFloat(),
                mWidth / 2.toFloat(),
                mBackgroundPaint
            )
        }
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    init {
        initPaint()
    }
}