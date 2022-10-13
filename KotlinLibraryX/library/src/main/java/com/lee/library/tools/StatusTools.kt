package com.lee.library.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 状态栏工具
 * @author jv.lee
 * @date 2019/4/5
 */
object StatusTools {

    /**
     * 设置沉浸式状态栏
     */
    fun Window.statusBar() {
        // 内容全屏化
        WindowCompat.setDecorFitsSystemWindows(this, false)

        // systemBar透明设置
        statusBarColor = Color.TRANSPARENT
        navigationBarColor = Color.TRANSPARENT

        // 处理contentView与navigationBar间距
        ViewCompat.setOnApplyWindowInsetsListener(decorView.findViewById(android.R.id.content)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * 兼容状态栏颜色控制
     * 高版本可动态修改状态栏图标样式无需处理
     * 5.0 5.1版本无法设置深色状态栏图标，所以设置一个半透明状态栏背景兼容。
     * 4.4有黑边阴影且无法设置状态栏颜色所以无视。
     */
    fun Window.compatStatusBar() {
        statusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Color.TRANSPARENT
        } else {
            Color.parseColor("#33000000")
        }
    }

    /**
     * 设置深色状态栏icon颜色
     */
    fun Window.setDarkStatusIcon() {
        val insetsController = WindowCompat.getInsetsController(this, decorView)
        insetsController.isAppearanceLightStatusBars = true
    }

    /**
     * 设置浅色状态栏icon颜色
     */
    fun Window.setLightStatusIcon() {
        val insetsController = WindowCompat.getInsetsController(this, decorView)
        insetsController.isAppearanceLightStatusBars = false
    }

    /**
     * 全屏模式
     * Activity在onResume中调用 防止横竖屏切换
     *
     * @param isFull 是否进入全屏模式
     */
    fun Window.fullWindow(isFull: Boolean) {
        val insetsController = WindowCompat.getInsetsController(this, decorView)
        if (isFull) {
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            setBangsFull()
        } else {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    /**
     * 设置刘海屏内容扩充至状态栏
     * API >= 28
     */
    private fun Window.setBangsFull() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = attributes
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            this.attributes = attributes
        }
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


}