package com.lee.library.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import com.lee.library.extensions.height
import com.lee.library.extensions.isLandscape
import com.lee.library.extensions.isPortrait
import com.lee.library.extensions.width

/**
 * 屏幕适配帮助类
 * @author jv.lee
 * @date 2021/9/2
 */
object ScreenDensityTools : ComponentCallbacks {

    private const val WIDTH = 360f
    var scale = 0f // 屏幕缩放倍数
    private var density: Float = 0f // Application的DisplayMetrics
    private var scaledDensity: Float = 0f //
    private lateinit var metrics: DisplayMetrics // Application的Density

    /**
     * 在Application中初始化Metrics
     */
    fun init(context: Context) {
        // 判断是否需要初始化
        if (density != 0f) return

        // 初始化
        metrics = context.resources.displayMetrics
        density = metrics.density
        scaledDensity = metrics.scaledDensity

        // 监听字体变化
        context.registerComponentCallbacks(this)
    }

    /**
     * 在Activity中初始化Density
     * @param ruler UI的设计宽度，默认为420dp
     */
    fun init(
        activity: Activity,
        ruler: Float = WIDTH,
        @Density flag: Int = Density.SHORT_SIDE_BASED
    ) {
        val width = width()
        val height = height()
        val pixels = when (flag) {
            Density.WIDTH_BASED -> width
            Density.HEIGHT_BASED -> height
            Density.SHORT_SIDE_BASED -> if (activity.isPortrait) width else height
            Density.LONG_SIDE_BASED -> if (activity.isLandscape) width else height
            else -> 0
        }
        init(activity, pixels / ruler)
    }

    /**
     * 在Activity中初始化Density
     * @param portRuler UI竖屏时的设计宽度，默认为420dp
     * @param landRuler UI横屏时的设计宽度，默认为980dp
     */
    fun init(activity: Activity, portRuler: Float = WIDTH, landRuler: Float) {
        val pixels = width()
        val ruler = if (activity.isLandscape) landRuler else portRuler
        init(activity, pixels / ruler)
    }

    /**
     * 初始化Activity的Density
     */
    private fun init(activity: Activity, density: Float) {
        activity.resources.displayMetrics.density = density
        activity.resources.displayMetrics.densityDpi = (160 * density).toInt()
        activity.resources.displayMetrics.scaledDensity = density * (scaledDensity / this.density)
        scale = this.density / density

        setBitmapDensity(activity.resources.displayMetrics.densityDpi)
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
        } catch (_: ClassNotFoundException) {
        } catch (_: NoSuchFieldException) {
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
        // 字体改变后,将appScaledDensity重新赋值
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