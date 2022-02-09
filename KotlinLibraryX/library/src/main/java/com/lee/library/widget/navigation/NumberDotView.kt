package com.lee.library.widget.navigation

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
    private var dotLineColor = Color.WHITE

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
        gravity = Gravity.CENTER
        includeFontPadding = false
        setTextColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        if (visibleView()) {
            buildDotView()
            super.onDraw(canvas)
        } else {
            background = null
        }
    }

    private fun buildDotView() {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(dotBackgroundColor)
        gradientDrawable.cornerRadius = height.toFloat()
        gradientDrawable.setStroke(context.dp2px(1).toInt(), dotLineColor)
        background = gradientDrawable
        minWidth = height
        initPadding()
    }

    fun setNumber(count: Int) {
        text = parseNumberStr(count)
        invalidate()
    }

    fun setDotBackgroundColor(@ColorInt color: Int) {
        dotBackgroundColor = color
        invalidate()
    }

    fun setDotLineColor(color: Int) {
        this.dotLineColor = color
        postInvalidate()
    }

    private fun parseNumberStr(number: Int): String {
        return if (number >= 999) MAX_VALUE else if (number <= 0) "" else number.toString()
    }

    private fun initPadding() {
        if (TextUtils.isEmpty(text)) {
            setPadding(0, 0, 0, 0)
            return
        }
        if (text.length > 1) {
            setPadding(
                context.dp2px(6).toInt(),
                context.dp2px(2).toInt(),
                context.dp2px(6).toInt(),
                context.dp2px(2).toInt()
            )
        } else {
            setPadding(
                context.dp2px(2).toInt(),
                context.dp2px(2).toInt(),
                context.dp2px(2).toInt(),
                context.dp2px(2).toInt()
            )
        }
    }

    private fun visibleView(): Boolean {
        return !(TextUtils.isEmpty(text) || TextUtils.equals(text, "0"))
    }

}