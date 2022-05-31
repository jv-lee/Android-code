package com.lee.library.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.lee.library.R
import com.lee.library.extensions.dp2px

/**
 * 状态按钮 可设置点击状态不同颜色 及锁定点击状态颜色
 * @see pressedBackgroundColor
 * @see pressedTextColor
 * @see pressedStrokeColor
 *
 * @see normalBackgroundColor
 * @see normalTextColor
 * @see normalStrokeColor
 *
 * @see disableBackgroundColor
 * @see disableTextColor
 * @see disableStrokeColor
 *
 * @see strokeWidth
 * @see buttonRadius
 * @see buttonDisable
 * @see rippleMode
 *
 * @author jv.lee
 * @date 2020/9/16
 */
@Keep
class SelectorTextView : AppCompatTextView {

    /**
     * 按下时背景颜色
     */
    private var pressedBackgroundColor: Int = 0

    /**
     * 默认背景颜色
     */
    private var normalBackgroundColor: Int = 0

    /**
     * 锁定点击时背景颜色
     */
    private var disableBackgroundColor: Int = 0

    /**
     * 按下时文字颜色
     */
    private var pressedTextColor: Int = 0

    /**
     * 默认文字颜色
     */
    private var normalTextColor: Int = 0

    /**
     * 锁定点击时文字颜色
     */
    private var disableTextColor: Int = 0

    /**
     * 按下时边框颜色
     */
    private var pressedStrokeColor: Int = 0

    /**
     * 默认边框颜色
     */
    private var normalStrokeColor: Int = 0

    /**
     * 锁定点击时边框颜色
     */
    private var disableStrokeColor: Int = 0

    /**
     * 点击扩散效果 noneMode：无 stateMode：扩散并且保持点击颜色变更 defaultMode：默认扩散效果
     */
    private var rippleMode: Int = 0

    /**
     * 边框宽度
     */
    private var strokeWidth: Float = 0F

    /**
     * 边角圆角度
     */
    private var buttonRadius: Float = 0F

    /**
     * 是否可点击状态
     */
    private var buttonDisable: Boolean = false

    private var disableBackgroundDrawable: GradientDrawable? = null
    private var stateBackgroundDrawable: StateListDrawable? = null
    private var rippleBackgroundDrawable: Drawable? = null
    private var stateTextColorDrawable: ColorStateList? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(
        context,
        attributes,
        defStyle
    ) {
        context.obtainStyledAttributes(attributes, R.styleable.SelectorTextView).run {
            pressedBackgroundColor = getColor(
                R.styleable.SelectorTextView_pressedBackgroundColor,
                Color.TRANSPARENT
            )
            normalBackgroundColor = getColor(
                R.styleable.SelectorTextView_normalBackgroundColor,
                Color.TRANSPARENT
            )
            pressedTextColor = getColor(
                R.styleable.SelectorTextView_pressedTextColor,
                ContextCompat.getColor(context, R.color.baseDarkColor)
            )
            normalTextColor = getColor(
                R.styleable.SelectorTextView_normalTextColor,
                ContextCompat.getColor(context, R.color.baseDarkColor)
            )
            disableBackgroundColor = getColor(
                R.styleable.SelectorTextView_disableBackgroundColor,
                Color.TRANSPARENT
            )
            disableTextColor = getColor(
                R.styleable.SelectorTextView_disableTextColor,
                ContextCompat.getColor(context, R.color.baseDarkColor)
            )
            pressedStrokeColor = getColor(
                R.styleable.SelectorTextView_pressedStrokeColor,
                Color.TRANSPARENT
            )
            normalStrokeColor = getColor(
                R.styleable.SelectorTextView_normalStrokeColor,
                Color.TRANSPARENT
            )
            disableStrokeColor = getColor(
                R.styleable.SelectorTextView_disableStrokeColor,
                Color.TRANSPARENT
            )
            strokeWidth = getDimension(R.styleable.SelectorTextView_strokeWidth, 0f)
            buttonRadius =
                getDimension(R.styleable.SelectorTextView_buttonRadius, context.dp2px(10))
            buttonDisable = getBoolean(R.styleable.SelectorTextView_buttonDisable, false)
            rippleMode = getInt(R.styleable.SelectorTextView_rippleMode, 0)
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

        createRippleBackgroundDrawable()

        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_pressed)
        states[1] = intArrayOf()
        stateTextColorDrawable =
            ColorStateList(states, intArrayOf(pressedTextColor, normalTextColor))

        gravity = Gravity.CENTER

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
            background =
                if (rippleMode != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rippleBackgroundDrawable
                } else {
                    stateBackgroundDrawable
                }
            setTextColor(stateTextColorDrawable)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        isClickable = !buttonDisable
    }

    private fun createRippleBackgroundDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (rippleMode) {
                1 -> {
                    rippleBackgroundDrawable = RippleDrawable(
                        ColorStateList.valueOf(pressedBackgroundColor),
                        stateBackgroundDrawable,
                        getShape()
                    )
                }
                2 -> {
                    rippleBackgroundDrawable = RippleDrawable(
                        ColorStateList.valueOf(pressedBackgroundColor),
                        createRippleContentDrawable(),
                        getShape()
                    )
                }
            }
        }
    }

    private fun createRippleContentDrawable(): Drawable {
        val drawable = GradientDrawable()
        drawable.setColor(normalBackgroundColor)
        drawable.cornerRadius = buttonRadius
        drawable.setStroke(strokeWidth.toInt(), normalStrokeColor)
        return drawable
    }

    private fun getShape(): Drawable {
        return ShapeDrawable(object : RectShape() {
            override fun draw(canvas: Canvas, paint: Paint) {
                val rect = RectF(0F, 0F, width, height)
                canvas.drawRoundRect(rect, buttonRadius, buttonRadius, paint)
            }
        })
    }

    fun setPressBackgroundColor(color: Int) {
        pressedBackgroundColor = color
        initBackground()
    }

    fun setNormalBackgroundColor(color: Int) {
        normalBackgroundColor = color
        initBackground()
    }

    fun setDisableBackgroundColor(color: Int) {
        disableBackgroundColor = color
        initBackground()
    }

    fun setPressTextColor(color: Int) {
        pressedTextColor = color
        initBackground()
    }

    fun setNormalTextColor(color: Int) {
        normalTextColor = color
        initBackground()
    }

    fun setDisableTextColor(color: Int) {
        disableTextColor = color
        initBackground()
    }

    fun setPressStrokeColor(color: Int) {
        pressedStrokeColor = color
        initBackground()
    }

    fun setNormalStrokeColor(color: Int) {
        normalStrokeColor = color
        initBackground()
    }

    fun setDisableStrokeColor(color: Int) {
        disableStrokeColor = ContextCompat.getColor(context, color)
        initBackground()
    }

    fun performPressed() {
        preformPressed(true)
        postDelayed({
            preformPressed(false)
        }, 300)
    }

    fun preformPressed(enable: Boolean) {
        if (enable) {
            setButtonDisable(false)
            isPressed = true
        } else {
            isPressed = false
            setButtonDisable(true)
        }
    }

}