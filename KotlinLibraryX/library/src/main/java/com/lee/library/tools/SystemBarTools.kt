package com.lee.library.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 系统窗口工具栏（状态栏、导航栏、软键盘） 工具类扩展函数
 * @author jv.lee
 * @date 2019/4/5
 */
object SystemBarTools {
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
            view.setPadding(0, 0, 0, windowInsets.navigationBarHeight())
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
     * 显示软键盘
     */
    fun Window.showSoftInput() {
        val insetsController = WindowCompat.getInsetsController(this, decorView)
        insetsController.show(WindowInsetsCompat.Type.ime())
    }

    /**
     * 隐藏软键盘
     */
    fun Window.hideSoftInput() {
        val insetsController = WindowCompat.getInsetsController(this, decorView)
        insetsController.hide(WindowInsetsCompat.Type.ime())
    }

    /**
     * 软键盘是否显示
     */
    fun Window.hasSoftInputShow(): Boolean {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(decorView.windowToken, 0)
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

    /**
     * 获取键盘高度
     */
    fun WindowInsetsCompat.imeHeight(): Int {
        return getInsets(WindowInsetsCompat.Type.ime()).bottom
    }


    /**
     * 点击任意view隐藏输入法
     */
    @SuppressLint("ClickableViewAccessibility")
    fun Window.parentTouchHideSoftInput(childView: View? = null) {
        val view = childView ?: decorView
        view.setOnTouchListener { _, _ ->
            if (hasSoftInputShow()) {
                hideSoftInput()
            }
            false
        }
    }

    /**
     * windowInsets作用域
     */
    fun View.runWindowInsets(
        lifecycleOwner: LifecycleOwner? = findViewTreeLifecycleOwner(),
        block: WindowInsetsCompat .() -> Unit
    ) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            ViewCompat.getRootWindowInsets(this)?.run(block)
        }
        lifecycleWindowGlobalLayout(listener, lifecycleOwner)
    }

    /**
     * 监听键盘弹起更该viewPaddingBottom值
     */
    fun View.softInputBottomPaddingChange(
        lifecycleOwner: LifecycleOwner? = findViewTreeLifecycleOwner(),
        open: () -> Unit = {},
        close: () -> Unit = {},
    ) {
        // 默认键盘未展开 过滤重复开关回调
        val isClose = AtomicBoolean(true)
        runWindowInsets(lifecycleOwner) {
            if (isVisible(WindowInsetsCompat.Type.ime())) {
                if (isClose.compareAndSet(true, false)) {
                    open()
                    setPadding(0, 0, 0, imeHeight() - navigationBarHeight())
                }
            } else {
                if (isClose.compareAndSet(false, true)) {
                    close()
                    setPadding(0, 0, 0, 0)
                }
            }
        }
    }

    private fun View.lifecycleWindowGlobalLayout(
        listener: ViewTreeObserver.OnGlobalLayoutListener,
        lifecycleOwner: LifecycleOwner?
    ) {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        viewTreeObserver.addOnGlobalLayoutListener(listener)
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        viewTreeObserver.removeOnGlobalLayoutListener(listener)
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        source.lifecycle.removeObserver(this)
                    }
                    else -> {
                    }
                }
            }
        })
    }
}