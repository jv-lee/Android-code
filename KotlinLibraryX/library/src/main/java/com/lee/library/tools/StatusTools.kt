package com.lee.library.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toolbar

/**
 * 状态栏工具
 *
 * @author jv.lee
 * @date 2019/4/5
 */
object StatusTools {

    /**
     * 设置沉浸式状态栏
     *
     * @param navigationBarTranslucent 导航栏是否设置为透明
     */
    @SuppressLint("ObsoleteSdkInt")
    fun Window.statusBar(navigationBarTranslucent: Boolean = false) {
        //5.0以设置沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色调整
            statusBarColor = Color.TRANSPARENT
            var visibility = decorView.systemUiVisibility
            //布局内容全屏展示
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //隐藏虚拟导航栏
//            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            //防止内容区域大小发生变化
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = visibility
            //4.0设置
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置沉浸式 状态栏
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    /**
     * 保持原有flag 设置深色状态栏颜色
     *
     */
    fun Activity.setDarkStatusIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val originFlag = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility =
                originFlag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 保留原有flag 清除深色状态栏颜色
     *
     */
    fun Activity.setLightStatusIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val originFlag = window.decorView.systemUiVisibility
            //使用异或清除SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility =
                originFlag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    /**
     * 设置导航栏颜色
     *
     * @param color
     */
    @SuppressLint("ObsoleteSdkInt")
    fun Activity.setNavigationBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = color
        }
    }

    /**
     * 全屏模式
     * Activity在onResume中调用 防止横竖屏切换
     *
     * @param activity
     */
    fun Activity.fullWindow(isFull: Boolean) {
        //1.设置全屏幕
        val flags: Int
        val curApiVersion = Build.VERSION.SDK_INT
        flags = if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        } else {
            // touch the screen, the navigation bar will show
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        if (isFull) {
            window.decorView.systemUiVisibility = flags
            setBangsFull()
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            window.statusBar(false)
        }
    }

    /**
     * 设置刘海屏内容扩充至状态栏
     * API >= 28
     */
    private fun Activity.setBangsFull() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    fun Context.statusBarHeight(): Int {
        val resId =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

    /**
     * 获取导航栏高度
     *
     * @return
     */
    fun Context.navigationBarHeight(): Int {
        val resId =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resId > 0 && checkHasNavigationBar()) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

    /**
     * 判断是否存在导航栏
     *
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    fun Context.checkHasNavigationBar(): Boolean {
        return try {
            val windowManager = (this as Activity).windowManager
            val d = windowManager.defaultDisplay
            val realDisplayMetrics = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                d.getRealMetrics(realDisplayMetrics)
            }
            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels
            realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * activity中无法直接使用 根据适合时机view在渲染成功后调用 否则需要主动调用 view.post{}
     */
    fun Context.getContentHeight(): Int {
        return try {
            val content =
                (this as Activity).window.decorView.findViewById<View>(android.R.id.content)
            content.measuredHeight
        } catch (e: Exception) {
            0
        }
    }

    /**
     * 设置自定义toolbar高度
     *
     * @param view
     */
    fun Context.setStatusPadding(view: View) {
        val layoutParams = view.layoutParams
        val statusHeight = statusBarHeight()
        layoutParams.height += statusHeight
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + statusHeight,
            view.paddingRight,
            view.paddingBottom
        )
    }

    fun Context.setStatusPadding(view: Toolbar) {
        val statusHeight = statusBarHeight()
        view.setPadding(0, statusHeight, 0, 0)
    }
}