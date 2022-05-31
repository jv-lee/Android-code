package com.lee.library.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.lee.library.R

/**
 *
 * @author jv.lee
 * @date 2021/1/19
 */
class BackgroundConstraintLayout constructor(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private var layoutBackgroundColor: Int
    private var layoutStrokeColor: Int
    private var layoutStrokeWidth: Float
    private var layoutRadius: Float

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.BackgroundConstraintLayout).run {
            layoutBackgroundColor = getColor(
                R.styleable.BackgroundConstraintLayout_layoutBackgroundColor,
                Color.TRANSPARENT
            )
            layoutStrokeColor = getColor(
                R.styleable.BackgroundConstraintLayout_layoutStrokeColor,
                Color.TRANSPARENT
            )
            layoutStrokeWidth =
                getDimension(R.styleable.BackgroundConstraintLayout_layoutStrokeWidth, 0f)
            layoutRadius = getDimension(R.styleable.BackgroundConstraintLayout_layoutRadius, 0f)

            recycle()
        }
        initBackground()
    }

    private fun initBackground() {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(layoutBackgroundColor)
        gradientDrawable.cornerRadius = layoutRadius
        gradientDrawable.setStroke(layoutStrokeWidth.toInt(), layoutStrokeColor)
        background = gradientDrawable
    }

}