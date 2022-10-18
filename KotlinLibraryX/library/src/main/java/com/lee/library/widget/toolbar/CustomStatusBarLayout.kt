package com.lee.library.widget.toolbar

import android.content.Context
import android.util.AttributeSet

/**
 * 自定义状态栏填充view 占位符View
 * @author jv.lee
 * @date 2020/4/14
 */
class CustomStatusBarLayout : CustomToolbarLayout {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    )

    override fun initStatusBarPadding() {}

    override fun initLayoutHeight(): Int {
        return getStatusBarHeight()
    }

}