package com.lee.adapter.adapter

import android.view.LayoutInflater
import com.lee.adapter.R
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem
import com.lee.library.widget.linear.LinearListView

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentHorizontalItem : LeeViewItem<ContentData> {
    override fun getItemLayout() = R.layout.item_content_horizontal

    override fun openClick() = true

    override fun openShake() = true

    override fun openRecycler() = false

    override fun isItemView(entity: ContentData?, position: Int) =
        entity?.type == ViewType.HORIZONTAL

    override fun convert(holder: LeeViewHolder?, entity: ContentData?, position: Int) {
        holder ?: return
        entity ?: return

        val linearListView = holder.getView<LinearListView>(R.id.linear_list_view)
        if (linearListView.childCount != 0) return
        for (index in 1..6) {
            linearListView.addView(
                LayoutInflater.from(linearListView.context)
                    .inflate(R.layout.item_image, linearListView, false)
            )
        }
    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: ContentData?, position: Int) {

    }

}