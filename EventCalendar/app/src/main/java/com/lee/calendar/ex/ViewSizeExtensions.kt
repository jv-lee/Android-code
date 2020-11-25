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
fun Context.dp2px(dpValue: Int): Float {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}

/**
 * px转dp
 *
 * @param context 上下文
 * @param pxValue px值
 * @return dp值
 */
fun Context.px2dp(pxValue: Int): Float {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f)
}

/**
 * sp转px
 *
 * @param context 上下文
 * @param spValue sp值
 * @return px值
 */
fun Context.sp2px(spValue: Int): Float {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f)
}

/**
 * px转sp
 *
 * @param context 上下文
 * @param pxValue px值
 * @return sp值
 */
fun Context.px2sp(pxValue: Float): Float {
    val fontScale = resources.displayMetrics.scaledDensity
    return pxValue / fontScale + 0.5f
}