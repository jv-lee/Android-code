package com.lee.library.dialog.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import com.lee.library.extensions.dp2px
import com.lee.library.utils.StatusUtil


/**
 * @author jv.lee
 * @date 2020/9/10
 * @description
 */
abstract class BaseDialog constructor(
    val mContext: Context,
    theme: Int,
    layoutId: Int,
    isCancel: Boolean = true
) :
    Dialog(mContext, theme) {

    init {
        setContentView(layoutId)
        setFullWindow(mContext)
        setBackDismiss(isCancel)
        bindView()
        bindData()
    }

    override fun show() {
        if (mContext is Activity) {
            if (mContext.isFinishing) {
                return
            }
        }
        super.show()
    }

    override fun dismiss() {
        if (mContext is Activity) {
            if (mContext.isFinishing) {
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
     * 绑定view
     */
    protected abstract fun bindView()

    /**
     * 绑定数据
     */
    protected open fun bindData() {}

}

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
 */
fun Dialog.setFullWindow(context:Context?) {
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

    val navigationHeight = StatusUtil.getNavigationBarHeight(context)

    val dm = DisplayMetrics()
    //获取包含状态栏及导航栏的屏幕size
    window.windowManager.defaultDisplay.getRealMetrics(dm)
    window.setLayout(
        dm.widthPixels,
        dm.heightPixels - navigationHeight//设置window内容区域高度 减去 导航栏高度
    )
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
