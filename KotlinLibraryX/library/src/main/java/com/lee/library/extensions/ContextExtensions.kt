/*
 * context扩展函数帮助类
 * @author jv.lee
 * @date 2020/4/1
 */
package com.lee.library.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.lee.library.base.ApplicationExtensions.app

/**
 * 状态栏高度
 */
val Context.statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        val resId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

/**
 * 导航栏高度
 */
val Context.navigationBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        val resId =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

/**
 * 判断设备是否处于竖屏状态
 * Check if the device orientation is portrait.
 */
val Context.isPortrait
    get() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * 判断设备是否处于横屏状态
 * Check if the device orientation is landscape.
 */
val Context.isLandscape
    get() = this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.windowManager
    get() = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun width(navigation: Boolean = true): Int = if (navigation) Screen().width else Display().width

fun height(hasStatus: Boolean = true, navigation: Boolean = true): Int {
    val screen = Screen()
    val display = Display()
    return when {
        hasStatus && navigation -> screen.height
        navigation -> screen.height - app.statusBarHeight
        hasStatus -> display.height()
        else -> display.height
    }
}

internal data class Screen(
    private val display: android.view.Display = app.windowManager.defaultDisplay
) {

    @SuppressLint("ObsoleteSdkInt")
    private val point = Point().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(this)
        } else {
            display.getSize(this)
        }
    }

    /**
     * 屏幕宽度，包含虚拟键，单位px
     */
    val width: Int = point.x

    /**
     * 屏幕高度，包含虚拟键和状态栏，单位px
     */
    val height: Int = point.y
}

internal data class Display(private val metrics: DisplayMetrics = app.resources.displayMetrics) {

    /**
     * 展示宽度，不包含虚拟键，单位px
     */
    val width: Int = metrics.widthPixels

    /**
     * 展示高度，不包含虚拟键与状态栏，单位px
     */
    val height: Int = height() - app.statusBarHeight

    /**
     * 展示高度，包含状态栏，不包含虚拟键，单位px
     */
    fun height(): Int = metrics.heightPixels
}

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
fun Context.applyDimension(value: Float, unit: Int): Float {
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

/**
 * 获取当前主题颜色属性值
 * @param attrId 属性值
 */
fun Context.getThemeColor(
    @AttrRes attrId: Int,
    @ColorRes defaultId: Int = android.R.color.transparent
): Int {
    return theme.obtainStyledAttributes(intArrayOf(attrId))
        .getColor(0, ContextCompat.getColor(this, defaultId))
}

/**
 * 获取当前主题像素值属性值
 * @param attrId 属性值
 */
fun Context.getThemeDimension(
    @AttrRes attrId: Int,
    defaultValue: Float = 0F
): Float {
    return theme.obtainStyledAttributes(intArrayOf(attrId))
        .getDimension(0, defaultValue)
}

/**
 * 获取颜色stateList
 * @param color 颜色取值resourceID
 */
fun Context.getColorStateListCompat(@ColorRes color: Int): ColorStateList {
    val colorResource = ContextCompat.getColor(this, color)
    val colors = intArrayOf(colorResource, colorResource)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_pressed)
    states[1] = intArrayOf()
    return ColorStateList(states, colors)
}

/**
 * 获取颜色stateList
 * @param checkColor 选中颜色resourceID
 * @param color 默认颜色resourceID
 */
fun Context.getCheckedColorStateListCompat(
    @ColorRes checkColor: Int,
    @ColorRes color: Int
): ColorStateList {
    val checkColorResource = ContextCompat.getColor(this, checkColor)
    val colorResource = ContextCompat.getColor(this, color)
    val colors = intArrayOf(checkColorResource, colorResource)
    val states = arrayOfNulls<IntArray>(2)
    states[0] = intArrayOf(android.R.attr.state_checked)
    states[1] = intArrayOf()
    return ColorStateList(states, colors)
}