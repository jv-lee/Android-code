package com.lee.adapter.adapter

import android.content.Context
import com.lee.adapter.entity.ContentData
import com.lee.library.adapter.page.PagingAdapter

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentAdapter(context: Context, data: List<ContentData>) :
    PagingAdapter<ContentData>(context, data) {
    init {
        addItemStyles(ContentItem())
    }
}