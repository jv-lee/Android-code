package com.lee.adapter.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.lee.adapter.databinding.ViewGroupBindingBinding
import com.lee.library.extensions.inflate
import com.lee.library.extensions.setBackgroundColorCompat

/**
 * @author jv.lee
 * @date 2021/6/16

 */
class BindingViewGroup : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    //在ViewGroup中使用必须在onAttachedToWindow中去使用进行初始化 否则无法获取生命周期监听及时置空引用
    val binding by inflate {
        ViewGroupBindingBinding.inflate(it, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.constLayout.setBackgroundColorCompat(android.R.color.holo_orange_dark)
    }

}