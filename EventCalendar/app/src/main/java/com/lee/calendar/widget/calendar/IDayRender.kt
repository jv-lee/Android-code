package com.lee.calendar.widget.calendar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import com.lee.calendar.entity.DayEntity

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
abstract class IDayRender constructor(
    val context: Context,
    @StyleableRes attrs: IntArray,
    @StyleRes defStyleRes: Int
) {

    init {
        context.obtainStyledAttributes(null, attrs, 0, defStyleRes).run {
            initAttrs(this)
            recycle()
        }
    }

    abstract fun initAttrs(typedArray: TypedArray)

    abstract fun initParams(
        childSize: MonthView.ChildSize,
        canvas: Canvas,
        entity: DayEntity,
        index: Int)

    abstract fun draw()

}