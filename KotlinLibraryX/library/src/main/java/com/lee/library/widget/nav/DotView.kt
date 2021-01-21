package com.lee.library.widget.nav

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2019/5/7
 */
class DotView : View {

    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWidth = 0
    private var mHeight = 0
    private var backgroundColor = Color.RED

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.DotView).run {
                backgroundColor = getColor(R.styleable.DotView_dotBackground, Color.RED)
                recycle()
            }
        }
        mBackgroundPaint.let {
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

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawCircle(
            mWidth / 2.toFloat(),
            mHeight / 2.toFloat(),
            mWidth / 2.toFloat(),
            mBackgroundPaint
        )
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    fun setDotColor(@ColorInt color: Int) {
        mBackgroundPaint.color = color
        invalidate()
    }

}