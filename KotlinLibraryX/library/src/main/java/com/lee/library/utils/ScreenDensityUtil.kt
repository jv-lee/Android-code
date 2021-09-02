package com.lee.library.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager

/**
 * @author jv.lee
 * @data 2021/9/2
 * @description
 */
object ScreenDensityUtil : ComponentCallbacks {

    var scale = 0f                                  //屏幕缩放倍数
    private var density: Float = 0f                 //Application的DisplayMetrics
    private var scaledDensity: Float = 0f           //
    private lateinit var metrics: DisplayMetrics    //Application的Density

    /**
     * 在Application中初始化Metrics
     */
    fun init(application: Application) {
        app = application
        //获取application的DisplayMetrics
        metrics = application.resources.displayMetrics
        //判断是否需要初始化
        if (density != 0f) return
        //初始化
        density = metrics.density
        scaledDensity = metrics.scaledDensity
        //监听字体变化
        app.registerComponentCallbacks(this)
    }

    /**
     * 在Activity中初始化Density
     * @param ruler UI的设计宽度，默认为420dp
     */
    fun init(
        activity: Activity,
        ruler: Float = 420f,
        @Density flag: Int = Density.SHORT_SIDE_BASED
    ) {
        val f = activity.window.decorView.systemUiVisibility
        val sf1 = View.SYSTEM_UI_FLAG_FULLSCREEN
        val sf2 = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val nf = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        val sf = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val navigation = ((f and sf) == sf) || ((f and nf) == nf)
        val status = ((f and sf) == sf) || ((f and sf1) == sf1) || ((f and sf2) == sf2)
        init(activity, ruler, status, navigation, flag)
    }

    /**
     * 在Activity中初始化Density
     * @param ruler UI的设计宽度，默认为420dp
     * @param status 设置基准尺寸时是否包含标题栏
     * @param navigation 设置基准尺寸时是否包含虚拟键
     */
    fun init(
        activity: Activity,
        ruler: Float = 420f,
        status: Boolean = false,
        navigation: Boolean = false,
        @Density flag: Int = Density.SHORT_SIDE_BASED
    ) {
        val width = width(navigation)
        val height = height(status, navigation)
        val pixels = when (flag) {
            Density.WIDTH_BASED -> width
            Density.HEIGHT_BASED -> height
            Density.SHORT_SIDE_BASED -> if (activity.application.isPortrait) width else height
            Density.LONG_SIDE_BASED -> if (activity.application.isLandscape) width else height
            else -> 0
        }
        init(activity.resources.displayMetrics, pixels / ruler)
    }

    /**
     * 在Activity中初始化Density
     * @param portRuler UI竖屏时的设计宽度，默认为420dp
     * @param landRuler UI横屏时的设计宽度，默认为980dp
     */
    fun init(activity: Activity, portRuler: Float = 420f, landRuler: Float) {
        val f = activity.window.decorView.systemUiVisibility
        val nf = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        val sf = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val navigation = ((f and sf) == sf) || ((f and nf) == nf)
        init(activity, portRuler, landRuler, navigation)
    }

    /**
     * 在Activity中初始化Density
     * @param portRuler UI竖屏时的设计宽度，默认为420dp
     * @param landRuler UI横屏时的设计宽度，默认为980dp
     * @param navigation 设置基准尺寸时是否包含虚拟键
     */
    fun init(
        activity: Activity,
        portRuler: Float = 420f,
        landRuler: Float,
        navigation: Boolean = false
    ) {
        val pixels = width(navigation)
        val ruler = if (app.isLandscape) landRuler else portRuler
        init(activity.resources.displayMetrics, pixels / ruler)
    }

    /**
     * 初始化Activity的Density
     */
    private fun init(metrics: DisplayMetrics, density: Float) {
        metrics.density = density
        metrics.densityDpi = (160 * density).toInt()
        metrics.scaledDensity = density * (scaledDensity / this.density)
        scale = this.density / density

        setBitmapDensity(metrics.densityDpi)
    }

    /**
     * 保证在应用进入热启动之前 清除所以density修改 保证热启动闪屏ui 不被 density和系统初始化的density发生拉扯情况
     *
     * @param activity 需要取消的activity
     */
    fun resetDensity(activity: Activity) {
        activity.resources.displayMetrics.density = metrics.density
        activity.resources.displayMetrics.densityDpi = metrics.densityDpi
        activity.resources.displayMetrics.scaledDensity = metrics.scaledDensity
    }

    /**
     * 设置 Bitmap 的默认屏幕密度
     * 由于 Bitmap 的屏幕密度是读取配置的，需要使用反射强行修改
     * @param density 屏幕密度
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private fun setBitmapDensity(density: Int) {
        try {
            val cls = Class.forName("android.graphics.Bitmap")
            val field = cls.getDeclaredField("sDefaultDensity")
            field.isAccessible = true
            field.set(null, density)
            field.isAccessible = false
        } catch (e: ClassNotFoundException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 字体变化时的回调
     */
    override fun onLowMemory() {}

    /**
     * 字体变化时的回调
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        //字体改变后,将appScaledDensity重新赋值
        if (newConfig.fontScale > 0) scaledDensity = metrics.scaledDensity
    }


}

annotation class Density {
    companion object {
        var WIDTH_BASED = 1
        var HEIGHT_BASED = 2
        var LONG_SIDE_BASED = 3
        var SHORT_SIDE_BASED = 4
    }
}

lateinit var app: Application

/**
 * 判断设备是否处于竖屏状态
 * Check if the device orientation is portrait.
 */
val Application.isPortrait get() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * 判断设备是否处于横屏状态
 * Check if the device orientation is landscape.
 */
val Application.isLandscape get() = this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Application.windowManager get() = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun getPixel(id: Int) = app.resources.getDimensionPixelSize(id)

val status: Int
    get() {
        val id = app.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) getPixel(id) else 0
    }

fun width(navigation: Boolean): Int = if (navigation) Screen().width else Display().width

fun height(hasStatus: Boolean, navigation: Boolean): Int {
    val screen = Screen()
    val display = Display()
    return when {
        hasStatus && navigation -> screen.height
        navigation -> screen.height - status
        hasStatus -> display.height()
        else -> display.height
    }
}

internal data class Screen(private val display: Display = app.windowManager.defaultDisplay) {

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
    val height: Int = height() - status

    /**
     * 展示高度，包含状态栏，不包含虚拟键，单位px
     */
    fun height(): Int = metrics.heightPixels

}