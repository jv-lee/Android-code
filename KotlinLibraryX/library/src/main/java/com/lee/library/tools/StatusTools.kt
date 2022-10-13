package com.lee.library.tools

import android.graphics.Color
import android.os.Build
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
     * windowInsets作用域
     */
    fun Window.runWindowInsets(block: WindowInsetsCompat.() -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(decorView.findViewById(android.R.id.content)) { _, windowInsets ->
            block(windowInsets)
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * 判断是否存在导航栏
     */
    fun WindowInsetsCompat.hasNavigationBar(): Boolean {
        return isVisible(WindowInsetsCompat.Type.navigationBars()) && getInsets(
            WindowInsetsCompat.Type.navigationBars()
        ).bottom > 0
    }

    /**
     * 获取导航栏高度
     */
    fun WindowInsetsCompat.navigationBarHeight(): Int {
        return getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
    }

    /**
     * 获取状态栏高度
     */
    fun WindowInsetsCompat.statusBarHeight(): Int {
        return getInsets(WindowInsetsCompat.Type.statusBars()).top
    }

}