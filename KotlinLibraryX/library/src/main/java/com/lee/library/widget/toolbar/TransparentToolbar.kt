package com.lee.library.widget.toolbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet

/**
 * 自定义透明带标题的toolbar
 * @author jv.lee
 * @date 2020/4/14
 */
class TransparentToolbar : TitleToolbar {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)
    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    )

    override fun initBackground() {
        setBackgroundColor(Color.TRANSPARENT)
    }
}