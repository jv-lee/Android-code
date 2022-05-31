package com.lee.adapter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lee.adapter.R
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.adapter.item.ViewItem

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class ContentCommonItem :
    ViewItem<ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.COMMON

    override fun getItemView(context: Context, parent: ViewGroup): View =
        LayoutInflater.from(context).inflate(R.layout.item_content_common, parent, false)

    override fun convert(holder: BaseViewHolder, entity: ContentData, position: Int) {
        holder.getView<TextView>(R.id.tv_content).text =
            "COMMON VIEW_TYPE -> page in - ${entity.id} , index in - $position"
    }

}