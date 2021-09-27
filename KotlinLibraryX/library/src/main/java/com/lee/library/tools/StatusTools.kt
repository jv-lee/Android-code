package com.lee.library.tools

import android.R
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
     * @param window                   引用
     * @param navigationBarTranslucent 导航栏是否设置为透明
     */
    fun statusBar(
        window: Window,
        navigationBarTranslucent: Boolean
    ) {
        //5.0以设置沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色调整
            window.statusBarColor = Color.TRANSPARENT
            var visibility = window.decorView.systemUiVisibility
            //布局内容全屏展示
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //隐藏虚拟导航栏
//            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            //防止内容区域大小发生变化
            visibility = visibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = visibility
            //4.0设置
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置沉浸式 状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    /**
     * 保持原有flag 设置深色状态栏颜色
     *
     * @param activity
     */
    fun setDarkStatusIcon(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val originFlag = activity.window.decorView.systemUiVisibility
            activity.window.decorView.systemUiVisibility =
                originFlag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 保留原有flag 清除深色状态栏颜色
     *
     * @param activity
     */
    fun setLightStatusIcon(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val originFlag = activity.window.decorView.systemUiVisibility
            //使用异或清除SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility =
                originFlag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    /**
     * 设置导航栏颜色
     *
     * @param activity
     * @param color
     */
    fun setNavigationBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.navigationBarColor = color
        }
    }

    /**
     * 全屏模式
     * Activity在onResume中调用 防止横竖屏切换
     *
     * @param activity
     */
    fun fullWindow(activity: Activity, isFull: Boolean) {
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
            activity.window.decorView.systemUiVisibility = flags
            setBangsFull(activity)
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBar(activity.window, false)
        }
    }

    /**
     * 设置刘海屏内容扩充至状态栏
     * API >= 28
     *
     * @param activity
     */
    private fun setBangsFull(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = activity.window.attributes
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            activity.window.attributes = attributes
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else 0
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resId > 0 && checkHasNavigationBar(context)) {
            context.resources.getDimensionPixelSize(resId)
        } else 0
    }

    /**
     * 判断是否存在导航栏
     *
     * @param context
     * @return
     */
    fun checkHasNavigationBar(context: Context): Boolean {
        return try {
            val windowManager = (context as Activity).windowManager
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
    fun getContentHeight(context: Context?): Int {
        context ?: return 0
        return try {
            val content = (context as Activity).window.decorView.findViewById<View>(R.id.content)
            content.measuredHeight
        } catch (e: Exception) {
            0
        }
    }

    /**
     * 设置自定义toolbar高度
     *
     * @param context
     * @param view
     */
    fun setStatusPadding(context: Context, view: View) {
        val layoutParams = view.layoutParams
        val statusHeight = getStatusBarHeight(context)
        layoutParams.height += statusHeight
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + statusHeight,
            view.paddingRight,
            view.paddingBottom
        )
    }

    fun setStatusPadding(
        context: Context,
        view: Toolbar
    ) {
        val statusHeight = getStatusBarHeight(context)
        view.setPadding(0, statusHeight, 0, 0)
    }
}