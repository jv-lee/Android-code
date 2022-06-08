/*
 * dialog扩展函数帮助类
 * @author jv.lee
 * @date 2022/6/8
 */
package com.lee.library.dialog.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import com.lee.library.extensions.dp2px
import com.lee.library.tools.StatusTools.getContentHeight

/**
 * 设置底部dialogPadding值来实现默认高度
 */
fun Dialog.setBottomDialog(height: Int) {
    if (height == 0) return
    val window = window
    window?.run {
        decorView.setPadding(0, 0, 0, 0)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        setGravity(Gravity.START or Gravity.BOTTOM)
        val maxHeight = context.dp2px(height).toInt()
        if (dm.heightPixels < maxHeight) {
            setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            setLayout(dm.widthPixels, maxHeight)
        }
    }

}

/**
 * dialog设置全屏 兼容高版本刘海屏
 * 必须在setContentView之后
 * @param context 传入activity
 */
fun Dialog.setFullWindow(context: Context?) {
    val window = window
    window ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        // 延伸显示区域到刘海
        val lp: WindowManager.LayoutParams = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
        // 设置页面全屏显示
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    val contentHeight = context?.getContentHeight() ?: 0
    val dm = DisplayMetrics()
    //获取包含状态栏及导航栏的屏幕size
    window.windowManager.defaultDisplay.getRealMetrics(dm)
    //获取到了contentView的高度即设置为该高度 否则设置屏幕高度
    window.setLayout(dm.widthPixels, if (contentHeight == 0) dm.heightPixels else contentHeight)
    window.setGravity(Gravity.TOP)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    window.decorView.setPadding(0, 0, 0, 0)
}

/**
 * dialog禁止取消
 */
fun Dialog.setBackDismiss(isCancel: Boolean) {
    setCanceledOnTouchOutside(isCancel)
    if (!isCancel) {
        setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
    }
}