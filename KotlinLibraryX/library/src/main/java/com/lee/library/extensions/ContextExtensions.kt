package com.lee.library.extensions

import android.content.Context
import android.util.TypedValue

/**
 * dp转px
 * @param value dp值
 * @return px值
 */
fun Context.dp2px(value: Int): Float {
    val scale = resources.displayMetrics.density
    return (value * scale + 0.5f)
}

/**
 * px转dp
 *
 * @param value px值
 * @return dp值
 */
fun Context.px2dp(value: Int): Float {
    val scale = resources.displayMetrics.density
    return (value / scale + 0.5f)
}

/**
 * sp转px
 *
 * @param value sp值
 * @return px值
 */
fun Context.sp2px(value: Int): Float {
    val scale = resources.displayMetrics.scaledDensity
    return (value * scale + 0.5f)
}

/**
 * px转sp
 *
 * @param value px值
 * @return sp值
 */
fun Context.px2sp(value: Int): Float {
    val scale = resources.displayMetrics.scaledDensity
    return value / scale + 0.5f
}

/**
 * dimens值转sp
 */
fun Context.dimensToSp(dimens: Float): Float {
    val scale = resources.displayMetrics.density
    return dimens / scale
}

/**
 * 各种单位转换
 *
 * 该方法存在于TypedValue
 *
 * @param unit    单位
 * @param value   值
 * @return 转换结果
 */
fun Context.applyDimension(value: Float,unit: Int): Float {
    val metrics = resources.displayMetrics
    when (unit) {
        TypedValue.COMPLEX_UNIT_PX -> return value
        TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
        TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
        TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
        TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
        TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
        else -> {
        }
    }
    return 0F
}