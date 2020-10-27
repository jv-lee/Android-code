package com.lee.library.dialog.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import com.lee.library.utils.SizeUtil
import com.lee.library.utils.StatusUtil


/**
 * @author jv.lee
 * @date 2020/9/10
 * @description
 */
abstract class BaseDialog constructor(context: Context, theme: Int, cancel: Boolean = true) :
    Dialog(context, theme) {

    init {
        if (!cancel) setBackDismiss()
        setContentFullView(buildViewId())
        bindView()
    }

    override fun show() {
        if (context is Activity) {
            if ((context as Activity).isFinishing) {
                return
            }
        }
        super.show()
    }

    override fun dismiss() {
        if (context is Activity) {
            if ((context as Activity).isFinishing) {
                return
            }
        }
        super.dismiss()
    }

    override fun onBackPressed() {
        if (isShowing) {
            dismiss()
            return
        }
        super.onBackPressed()
    }

    /**
     * dialog的view
     *
     * @return 返回view的 layout 资源ID
     */
    protected abstract fun buildViewId(): Int

    /**
     * 绑定view
     */
    protected abstract fun bindView()

}

fun Dialog.setBottomDialog(height: Int) {
    val window = window
    window?.run {
        decorView.setPadding(0, 0, 0, 0)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        setGravity(Gravity.START or Gravity.BOTTOM)
        val maxHeight = SizeUtil.dp2px(context, height.toFloat())
        if (dm.heightPixels < maxHeight) {
            setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            setLayout(dm.widthPixels, maxHeight)
        }
    }

}

/**
 * dialog设置全屏
 */
fun Dialog.setContentFullView(layoutId: Int) {
    val window = window
    window ?: return
    //设置全屏
    window.setContentView(layoutId)

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

    val dm = DisplayMetrics()
    window.windowManager.defaultDisplay.getRealMetrics(dm)
    window.setLayout(
        dm.widthPixels,
        dm.heightPixels - StatusUtil.getNavigationBarHeight(window.context)
    )
    window.setGravity(Gravity.TOP)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
}

/**
 * dialog禁止取消
 */
fun Dialog.setBackDismiss() {
    setCanceledOnTouchOutside(false)
    setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
}
