package com.lee.library.widget.menu

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.lee.library.widget.toolbar.TitleToolbar

/**
 * @author jv.lee
 * @date 2020/4/22
 * @description
 */
class CustomPopupMenuHelper(var context: Context, var menuResId: Int) : View.OnClickListener {

    var toolbarClickListener: TitleToolbar.ClickListener? = null

    private val menuInflater by lazy { CustomMenuInflater(context) }

    private val rootView: View by lazy {
        menuInflater.apply { inflate(menuResId) }.buildMenuView().also {
            for (index in 0..it.childCount) {
                it.getChildAt(index)?.setOnClickListener(this)
            }
        }
    }

    val menuPW: PopupWindow by lazy {
        PopupWindow(
            rootView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            setBackgroundDrawable(ColorDrawable())
        }
    }


    override fun onClick(v: View?) {
        v?.let {
            menuPW.dismiss()
            toolbarClickListener?.menuItemClick(it)
        }
    }


}