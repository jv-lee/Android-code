package com.lee.camerax.base

import android.content.Context

/**
 * @author jv.lee
 * @date 2021/3/24
 * @description
 */
fun Context.dp2px(value: Int): Int {
    val scale = resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}