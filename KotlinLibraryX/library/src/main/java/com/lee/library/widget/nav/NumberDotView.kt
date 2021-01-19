package com.lee.library.widget.nav

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.lee.library.R
import com.lee.library.extensions.dp2px

/**
 * @author jv.lee
 * @date 2019/5/7
 */
class NumberDotView : AppCompatTextView {

    private var dotBackgroundColor = Color.RED

    internal companion object {
        const val MAX_VALUE = "999+"
    }

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
            context.obtainStyledAttributes(it, R.styleable.NumberDotView).run {
                dotBackgroundColor = getColor(R.styleable.NumberDotView_dotBackground, Color.RED)
                recycle()
            }
        }
    }

    private fun initParams() {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(dotBackgroundColor)
        gradientDrawable.cornerRadius = height.toFloat()
        background = gradientDrawable
        minWidth = height
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        includeFontPadding = false
        initPadding()
    }

    override fun onDraw(canvas: Canvas) {
        if (visibleView()) {
            initParams()
            super.onDraw(canvas)
        }
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.dotBackgroundColor = backgroundColor
        postInvalidate()
    }

    fun setNumber(count: Int) {
        text = parseNumberStr(count)
    }

    fun setDotBackgroundColor(@ColorInt color: Int) {
        dotBackgroundColor = color
        invalidate()
    }

    private fun parseNumberStr(number: Int): String {
        return if (number >= 999) MAX_VALUE else number.toString()
    }

    private fun initPadding() {
        if (TextUtils.isEmpty(text)) {
            setPadding(0, 0, 0, 0)
            return
        }
        if (text.length > 1) {
            setPadding(context.dp2px(4).toInt(), 0, context.dp2px(4).toInt(), 0)
        } else {
            setPadding(0, 0, 0, 0)
        }
    }

    private fun visibleView(): Boolean {
        return !TextUtils.isEmpty(text)
    }

}