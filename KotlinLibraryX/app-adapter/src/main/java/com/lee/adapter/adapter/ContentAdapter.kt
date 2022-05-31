package com.lee.adapter.adapter

import android.content.Context
import com.lee.adapter.entity.ContentData
import com.lee.library.adapter.base.BaseViewAdapter

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class ContentAdapter(context: Context, data: List<ContentData>) :
    BaseViewAdapter<ContentData>(context, data) {
    init {
        addItemStyles(ContentCommonItem())
        addItemStyles(ContentVerticalItem())
        addItemStyles(ContentHorizontalItem())
    }
}