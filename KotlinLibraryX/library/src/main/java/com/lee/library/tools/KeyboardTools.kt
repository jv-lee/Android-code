package com.lee.library.tools

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.lee.library.extensions.setMargin
import com.lee.library.extensions.statusBarHeight
import kotlin.math.abs

/**
 * 避免输入法面板遮挡
 * <p>在manifest.xml中activity中设置</p>
 * <p>android:windowSoftInputMode="adjustPan"</p>
 */

/**
 * @author jv.lee
 * @date 2020/9/4
 * @description
 */
object KeyboardTools {

    /**
     * 动态隐藏软键盘
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun Context.hideSoftInput() {
        var view = (this as? Activity)?.currentFocus
        if (view == null) {
            view = View(this)
        }
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 动态显示软键盘
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun Context.showSoftInput() {
        val activity = (this as? Activity) ?: return
        val view = activity.window.decorView
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    /**
     * 动态显示软键盘
     * @param view 触发的view
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun Context.showSoftInput(view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }


    /**
     * 切换键盘显示与否状态
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun Context.toggleSoftInput() {
        if (keyboardIsShow()) {
            hideSoftInput()
        } else {
            showSoftInput()
        }
    }

    /**
     * 判断软键盘是否弹出
     */
    fun Context.keyboardIsShow(): Boolean {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowToken = (this as? Activity)?.window?.decorView?.windowToken ?: return false
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * 点击任意view隐藏输入法
     */
    @SuppressLint("ClickableViewAccessibility")
    fun Context.parentTouchHideSoftInput(view: View) {
        view.setOnTouchListener { _, _ ->
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            if (keyboardIsShow()) {
                hideSoftInput()
            }
            false
        }
    }

    /**
     * 沉浸式状态栏 设置adjustResize 后 解决软键盘无法正常顶起解决方式
     */
    fun ViewGroup.adjustResizeStatusBar(
        window: Window,
        marginValue: Int = context.statusBarHeight
    ) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        fitsSystemWindows = true
        setMargin(top = -marginValue)
    }

    /**
     * 监听键盘弹起
     */
    inline fun View.keyboardObserver(
        crossinline openObserver: () -> Unit = {},
        crossinline closeObserver: () -> Unit = {},
        lifecycle: Lifecycle? = findViewTreeLifecycleOwner()?.lifecycle
    ) {
        var isOpen = false
        val keyboardHeight = 200
        val listener = {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)

            val height: Int = context.resources.displayMetrics.heightPixels
            // 获取键盘抬高的高度
            val diff: Int = height - rect.height()
            if (diff > keyboardHeight && !isOpen) {
                isOpen = true
                openObserver()
            } else if (diff < keyboardHeight && isOpen) {
                isOpen = false
                closeObserver()
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        })
    }

    /**
     * 监听键盘弹起
     */
    inline fun View.keyboardObserver(
        crossinline keyboardObserver: (Int) -> Unit = {},
        lifecycle: Lifecycle? = findViewTreeLifecycleOwner()?.lifecycle
    ) {
        val listener = {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)

            val height: Int = context.resources.displayMetrics.heightPixels
            // 获取键盘抬高的高度
            val diff: Int = height - rect.height()
            keyboardObserver(diff)


        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        })
    }

    /**
     * 打开键盘后移动rootView 该方式可使toolbar不动 ，只设置根View的marginBottom值来偏移
     * 只需给activity设置 android:windowSoftInputMode="stateHidden|adjustResize"
     */
    fun Window.keyboardOpenMoveView(
        rootView: ViewGroup,
        lifecycle: Lifecycle? = null
    ) {
        val decorView = decorView
        val statusBarHeight = decorView.context.statusBarHeight
        var isStatusDiff = false
        var statusDiff = 0

        val listener = {
            val r = Rect()
            //r will be populated with the coordinates of your view that area still visible.
            decorView.getWindowVisibleDisplayFrame(r)

            //get screen height and calculate the difference with the useable area from the r
            val height: Int = decorView.context.resources.displayMetrics.heightPixels
            var diff = (height - r.bottom)

            //部分机型 statusBar 的高度不会计算在屏幕显示高度内 所以做初始化判断 diff值是否等于statusBar高度 做处理
            if (abs(diff) == abs(statusBarHeight) && !isStatusDiff) {
                statusDiff = statusBarHeight
                isStatusDiff = true
            }
            diff += statusDiff

            //if it could be a keyboard add the padding to the view
            if (diff != 0) {
                // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
                //check if the padding is 0 (if yes set the padding for the keyboard)
                if (rootView.paddingBottom != diff) {
                    //set the padding of the contentView for the keyboard
                    rootView.setPadding(0, 0, 0, diff)
                }
            } else {
                //check if the padding is != 0 (if yes reset the padding)
                if (rootView.paddingBottom != 0) {
                    //reset the padding of the contentView
                    rootView.setPadding(0, 0, 0, 0)
                }
            }
        }
        decorView.viewTreeObserver.addOnGlobalLayoutListener(listener)


        lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    decorView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        })
    }

}