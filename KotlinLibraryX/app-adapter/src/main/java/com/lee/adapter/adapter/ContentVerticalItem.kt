package com.lee.adapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lee.adapter.R
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.item.ViewItem
import com.lee.library.widget.linear.LinearListView

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentVerticalItem :
    ViewItem<ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.VERTICAL

    override fun getItemView(context: Context, parent: ViewGroup): View =
        LayoutInflater.from(context).inflate(R.layout.item_content_vertical, parent, false)

    override fun convert(holder: BaseViewHolder, entity: ContentData, position: Int) {
        val linearListView = holder.getView<LinearListView>(R.id.linear_list_view)
        if (linearListView.childCount != 0) return
        for (index in 1..3) {
            linearListView.addView(
                LayoutInflater.from(linearListView.context)
                    .inflate(R.layout.item_image, linearListView, false)
            )
        }
    }

}