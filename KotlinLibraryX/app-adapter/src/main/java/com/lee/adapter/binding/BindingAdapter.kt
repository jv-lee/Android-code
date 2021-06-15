package com.lee.adapter.binding

import android.content.Context
import android.view.LayoutInflater
import com.lee.adapter.adapter.ContentCommonItem
import com.lee.adapter.adapter.ContentHorizontalItem
import com.lee.adapter.adapter.ContentVerticalItem
import com.lee.adapter.entity.ContentData
import com.lee.library.adapter.binding.ViewBindingAdapter


/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
class BindingAdapter(context: Context, data: List<ContentData>) :
    ViewBindingAdapter<ContentData>(context, data) {

    init {
        addItemStyles(BindingCommonItem())
        addItemStyles(BindingHorizontalItem())
        addItemStyles(BindingVerticalItem())
    }

}