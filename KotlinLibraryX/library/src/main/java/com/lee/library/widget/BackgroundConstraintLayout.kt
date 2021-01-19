package com.lee.library.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2021/1/19
 * @description
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
                ContextCompat.getColor(context, android.R.color.black)
            )
            layoutStrokeColor = getColor(
                R.styleable.BackgroundConstraintLayout_layoutStrokeColor,
                ContextCompat.getColor(context, android.R.color.black)
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