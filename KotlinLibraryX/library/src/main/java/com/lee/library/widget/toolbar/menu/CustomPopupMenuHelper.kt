package com.lee.library.widget.toolbar.menu

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.lee.library.R
import com.lee.library.extensions.dp2px
import com.lee.library.widget.toolbar.TitleToolbar

/**
 * @author jv.lee
 * @date 2020/4/22
 * @description 自定义popupMenu弹窗帮助类
 */
class CustomPopupMenuHelper(var context: Context, var menuResId: Int) : View.OnClickListener {

    var toolbarClickListener: TitleToolbar.ClickListener? = null

    private val menuInflater by lazy { CustomMenuInflater(context) }

    private val rootView: View by lazy {
        createCardView().also { card ->
            card.addView(menuInflater.apply { inflate(menuResId) }.buildMenuView().also {
                for (index in 0..it.childCount) {
                    it.getChildAt(index)?.setOnClickListener(this)
                }
            })
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

    private fun createCardView() = CardView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        useCompatPadding = true
        radius = context.dp2px(6)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.baseItemColor))
    }

}