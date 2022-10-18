package com.lee.library.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.lee.library.base.ApplicationExtensions.app

/**
 * 避免输入法面板遮挡
 * 在manifest.xml中activity中设置
 * android:windowSoftInputMode="adjustPan"
 * @author jv.lee
 * @date 2020/9/4
 */
object KeyboardTools {

    var imm: InputMethodManager =
        app.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    /**
     * 动态显示软键盘
     */
    fun Context.showSoftInput() {
        val activity = (this as? Activity) ?: return
        val view = activity.window.decorView
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm.showSoftInput(view, 0)
    }

    /**
     * 动态隐藏软键盘
     */
    fun Context.hideSoftInput() {
        var view = (this as? Activity)?.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 判断软键盘是否弹出
     */
    fun Context.keyboardIsShow(): Boolean {
        val windowToken = (this as? Activity)?.window?.decorView?.windowToken ?: return false
        return imm.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * 切换键盘显示与否状态
     */
    fun Context.toggleSoftInput() {
        if (keyboardIsShow()) {
            hideSoftInput()
        } else {
            showSoftInput()
        }
    }

    /**
     * 动态显示软键盘
     */
    fun View.showSoftInput() {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        imm.showSoftInput(this, 0)
    }

    /**
     * 点击任意view隐藏输入法
     */
    @SuppressLint("ClickableViewAccessibility")
    fun View.parentTouchHideSoftInput() {
        setOnTouchListener { _, _ ->
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            if (context.keyboardIsShow()) {
                context.hideSoftInput()
            }
            false
        }
    }

    /**
     * 监听键盘弹起更该viewPaddingBottom值
     */
    fun View.keyboardPaddingBottom(lifecycleOwner: LifecycleOwner? = findViewTreeLifecycleOwner()) {
        val keyboardMinHeight = 100
        var initDiff = 0
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)

            val height: Int = context.resources.displayMetrics.heightPixels
            // 获取键盘抬高的高度
            val diff: Int = height - rect.height()
            if (diff > keyboardMinHeight) {
                setPadding(0, 0, 0, diff - initDiff)
            } else {
                initDiff = diff
                setPadding(0, 0, 0, 0)
            }
        }
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

    /**
     * 监听键盘弹起
     */
    inline fun View.keyboardObserver(
        lifecycleOwner: LifecycleOwner? = findViewTreeLifecycleOwner(),
        crossinline open: () -> Unit = {},
        crossinline close: () -> Unit = {},
    ) {
        var isOpen = false
        val keyboardHeight = 200
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)

            val height: Int = context.resources.displayMetrics.heightPixels
            // 获取键盘抬高的高度
            val diff: Int = height - rect.height()
            if (diff > keyboardHeight && !isOpen) {
                isOpen = true
                open()
            } else if (diff < keyboardHeight && isOpen) {
                isOpen = false
                close()
            }
        }
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