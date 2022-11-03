package com.lee.library.interadp

import android.view.View
import com.lee.library.widget.toolbar.TitleToolbar

/**
 * TitleToolbar内部接口DLS实现
 * @author jv.lee
 * @date 2022/5/12
 */
fun TitleToolbar.setClickListener(init: TitleToolbarListenerDslImpl.() -> Unit) {
    val listener = TitleToolbarListenerDslImpl()
    listener.init()
    this.setClickListener(listener)
}

class TitleToolbarListenerDslImpl : TitleToolbar.ClickListener() {

    private var backClick: (() -> Unit)? = null
    private var moreClick: (() -> Unit)? = null
    private var menuItemClick: ((View) -> Unit)? = null

    fun backClick(method: () -> Unit) {
        backClick = method
    }

    fun moreClick(method: () -> Unit) {
        moreClick = method
    }

    fun menuItemClick(method: (View) -> Unit) {
        menuItemClick = method
    }

    override fun backClick() {
        backClick?.invoke()
    }

    override fun moreClick() {
        moreClick?.invoke()
    }

    override fun menuItemClick(view: View) {
        menuItemClick?.invoke(view)
    }
}