package com.lee.calendar.ex

import android.content.Context
import android.view.View

/**
 * @author jv.lee
 * @date 2020/11/13
 * @description
 */
/**
 * dp转px
 *
 * @param context 上下文
 * @param dpValue dp值
 * @return px值
 */
fun View.dp2px(context: Context, dpValue: Int): Float {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}

/**
 * px转dp
 *
 * @param context 上下文
 * @param pxValue px值
 * @return dp值
 */
fun View.px2dp(context: Context, pxValue: Int): Float {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f)
}

/**
 * sp转px
 *
 * @param context 上下文
 * @param spValue sp值
 * @return px值
 */
fun View.sp2px(context: Context, spValue: Int): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f)
}

/**
 * px转sp
 *
 * @param context 上下文
 * @param pxValue px值
 * @return sp值
 */
fun View.px2sp(context: Context, pxValue: Float): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return pxValue / fontScale + 0.5f
}