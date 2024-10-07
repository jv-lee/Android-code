package com.lee.adapter.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.adapter.R
import com.lee.adapter.databinding.ItemContentCommonBinding
import com.lee.adapter.databinding.ItemContentHorizontalBinding
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.adapter.item.ViewBindingItem

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class BindingHorizontalItem : ViewBindingItem<ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.HORIZONTAL

    override fun convert(holder: ViewBindingHolder, entity: ContentData, position: Int) {
        holder.getViewBinding<ItemContentHorizontalBinding>().apply {
            if (linearListView.childCount != 0) return
            for (index in 1..6) {
                linearListView.addView(
                    LayoutInflater.from(linearListView.context)
                        .inflate(R.layout.item_image, linearListView, false)
                )
            }
        }
    }

    override fun getItemViewBinding(context: Context, parent: ViewGroup): ViewBinding {
        return ItemContentCommonBinding.inflate(LayoutInflater.from(context), parent, false)
    }
}