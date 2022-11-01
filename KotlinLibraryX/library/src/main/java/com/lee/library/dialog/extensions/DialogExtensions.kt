/*
 * dialog扩展函数帮助类
 * @author jv.lee
 * @date 2022/6/8
 */
package com.lee.library.dialog.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.lee.library.extensions.dp2px
import com.lee.library.extensions.height
import com.lee.library.extensions.navigationBarHeight
import com.lee.library.extensions.width

/**
 * dialog设置全屏 兼容高版本刘海屏
 * 必须在setContentView之后
 */
fun Dialog.setFullWindow() {
    window?.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 设置页面全屏显示
            WindowCompat.setDecorFitsSystemWindows(this, false)
            // 延伸显示区域到刘海
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setLayout(width(), height() - context.navigationBarHeight)
        setGravity(Gravity.TOP)
        addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        decorView.setPadding(0, 0, 0, 0)
    }
}

/**
 * 设置底部dialogPadding值来实现默认高度
 */
fun Dialog.setBottomDialog(height: Int) {
    if (height == 0) return
    window?.run {
        decorView.setPadding(0, 0, 0, 0)
        val dm = context.resources.displayMetrics
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
 * dialog禁止取消
 */
fun Dialog.setBackDismiss(isCancel: Boolean) {
    setCanceledOnTouchOutside(isCancel)
    if (!isCancel) {
        setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
    }
}

/**
 * dialog隐藏阴影遮罩
 */
fun Dialog.hideCover(): Dialog {
    window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    return this
}