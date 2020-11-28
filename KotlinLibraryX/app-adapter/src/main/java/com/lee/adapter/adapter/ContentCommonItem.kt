package com.lee.adapter.adapter

import android.widget.TextView
import com.lee.adapter.R
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentCommonItem : LeeViewItem<ContentData> {
    override fun getItemLayout() = R.layout.item_content_common

    override fun openClick() = true

    override fun openShake() = true

    override fun openRecycler() = false

    override fun isItemView(entity: ContentData?, position: Int) = entity?.type == ViewType.COMMON

    override fun convert(holder: LeeViewHolder?, entity: ContentData?, position: Int) {
        holder ?: return
        entity ?: return

        holder.getView<TextView>(R.id.tv_content).text =
            "COMMON VIEW_TYPE -> page in - ${entity.id} , index in - $position"
    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: ContentData?, position: Int) {

    }

}