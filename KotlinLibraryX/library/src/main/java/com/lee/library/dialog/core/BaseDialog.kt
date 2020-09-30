package com.lee.library.dialog.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
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
        if(!cancel)setBackDismiss()
        setContentView(buildViewId())
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
fun Dialog.setFullWindow(layoutId: Int) {
    val window = window
    window ?: return
    window.setContentView(layoutId)
    //设置全屏
    val windowManager: WindowManager = window.windowManager
    val display: Display = windowManager.defaultDisplay
    val lp: WindowManager.LayoutParams = window.attributes
    lp.height = display.height //设置宽度
    lp.width = display.width //设置宽度
    window.attributes = lp
    window.decorView.setPadding(0, 0, 0, StatusUtil.getNavigationBarHeight(context))
}

/**
 * dialog禁止取消
 */
fun Dialog.setBackDismiss() {
    setCancelable(false)
    setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
}