package com.lee.library.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.lee.library.R
import com.lee.library.utils.SizeUtil

/**
 * @author jv.lee
 * @date 2020/9/16
 * @description 状态按钮 可设置点击状态不同颜色 及锁定点击状态颜色
 */
class SelectorButton constructor(context: Context, attributeSet: AttributeSet) :
    AppCompatButton(context, attributeSet) {

    private var pressedBackgroundColor: Int
    private var normalBackgroundColor: Int
    private var disableBackgroundColor: Int
    private var pressedTextColor: Int
    private var normalTextColor: Int
    private var disableTextColor: Int
    private var pressedStrokeColor: Int
    private var normalStrokeColor: Int
    private var disableStrokeColor: Int
    private var strokeWidth: Float
    private var buttonRadius: Float
    private var buttonDisable: Boolean
    private var disableBackgroundDrawable: GradientDrawable? = null
    private var stateBackgroundDrawable: StateListDrawable? = null
    private var stateTextColorDrawable: ColorStateList? = null

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.SelectorButton).run {
            pressedBackgroundColor = getColor(
                R.styleable.SelectorButton_pressedBackgroundColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
            normalBackgroundColor = getColor(
                R.styleable.SelectorButton_normalBackgroundColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
            pressedTextColor = getColor(
                R.styleable.SelectorButton_pressedTextColor,
                ContextCompat.getColor(context, android.R.color.white)
            )
            normalTextColor = getColor(
                R.styleable.SelectorButton_normalTextColor,
                ContextCompat.getColor(context, android.R.color.white)
            )
            disableBackgroundColor = getColor(
                R.styleable.SelectorButton_disableBackgroundColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
            disableTextColor = getColor(
                R.styleable.SelectorButton_disableTextColor,
                ContextCompat.getColor(context, android.R.color.white)
            )
            pressedStrokeColor = getColor(
                R.styleable.SelectorButton_pressedStrokeColor,
                ContextCompat.getColor(context, android.R.color.transparent)
            )
            normalStrokeColor = getColor(
                R.styleable.SelectorButton_normalStrokeColor,
                ContextCompat.getColor(context, android.R.color.transparent)
            )
            disableStrokeColor = getColor(
                R.styleable.SelectorButton_disableStrokeColor,
                ContextCompat.getColor(context, android.R.color.transparent)
            )
            strokeWidth = getDimension(R.styleable.SelectorButton_strokeWidth, 0f)
            buttonRadius = getDimension(
                R.styleable.SelectorButton_buttonRadius,
                SizeUtil.dp2px(context, 10F).toFloat()
            )
            buttonDisable = getBoolean(R.styleable.SelectorButton_buttonDisable, false)
            recycle()
        }
        initBackground()
    }

    private fun initBackground() {
        val pressedDrawable = GradientDrawable()
        pressedDrawable.setColor(pressedBackgroundColor)
        pressedDrawable.cornerRadius = buttonRadius
        pressedDrawable.setStroke(strokeWidth.toInt(), pressedStrokeColor)

        val normalDrawable = GradientDrawable()
        normalDrawable.setColor(normalBackgroundColor)
        normalDrawable.cornerRadius = buttonRadius
        normalDrawable.setStroke(strokeWidth.toInt(), normalStrokeColor)

        disableBackgroundDrawable = GradientDrawable()
        disableBackgroundDrawable?.setColor(disableBackgroundColor)
        disableBackgroundDrawable?.cornerRadius = buttonRadius
        disableBackgroundDrawable?.setStroke(strokeWidth.toInt(), disableStrokeColor)

        stateBackgroundDrawable = StateListDrawable()
        stateBackgroundDrawable?.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        stateBackgroundDrawable?.addState(intArrayOf(), normalDrawable)

        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_pressed)
        states[1] = intArrayOf()
        stateTextColorDrawable =
            ColorStateList(states, intArrayOf(pressedTextColor, normalTextColor))

        setButtonDisable(buttonDisable)
    }

    /**
     * @param disable true为锁定点击状态 false为解锁 可点击状态
     */
    fun setButtonDisable(disable: Boolean) {
        isClickable = !disable
        if (disable) {
            background = disableBackgroundDrawable
            setTextColor(disableTextColor)
        } else {
            background = stateBackgroundDrawable
            setTextColor(stateTextColorDrawable)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        isClickable = !buttonDisable
    }

}