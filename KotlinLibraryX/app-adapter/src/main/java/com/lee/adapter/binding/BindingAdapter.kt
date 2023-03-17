package com.lee.adapter.binding

import android.content.Context
import com.lee.adapter.entity.ContentData
import com.lee.library.adapter.binding.ViewBindingAdapter

/**
 *
 * @author jv.lee
 * @date 2021/6/15
 */
class BindingAdapter(context: Context) : ViewBindingAdapter<ContentData>(context) {

    init {
        addItemStyles(BindingCommonItem())
        addItemStyles(BindingHorizontalItem())
        addItemStyles(BindingVerticalItem())
    }
}