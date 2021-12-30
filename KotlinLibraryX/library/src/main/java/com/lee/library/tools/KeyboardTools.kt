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
import android.view.inputmethod.InputMethodManager
import com.lee.library.tools.StatusTools.statusBarHeight
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
     * 打开键盘后移动rootView 该方式可使toolbar不动 ，只设置根View的marginBottom值来偏移
     * 只需给activity设置 android:windowSoftInputMode="stateHidden|adjustResize"
     */
    fun Window.keyboardOpenMoveView(rootView: ViewGroup) {
        val decorView = decorView
        val statusBarHeight = decorView.context.statusBarHeight()
        var isStatusDiff = false
        var statusDiff = 0
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
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
    }

}