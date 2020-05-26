package com.lee.library.widget.toolbar

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

/**
 * @author jv.lee
 * @date 2020/4/14
 * @description 自定义透明带标题的toolbar
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
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

}