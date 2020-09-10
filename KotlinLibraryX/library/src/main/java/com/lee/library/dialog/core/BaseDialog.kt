package com.lee.library.dialog.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.lee.library.utils.SizeUtil


/**
 * @author jv.lee
 * @date 2020/9/10
 * @description
 */
abstract class BaseDialog constructor(context: Context, theme: Int) : Dialog(context, theme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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