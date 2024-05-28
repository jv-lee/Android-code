package com.lee.adapter.binding

import android.view.LayoutInflater
import com.lee.adapter.R
import com.lee.adapter.databinding.ItemContentVerticalBinding
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.adapter.item.ViewBindingItem

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class BindingVerticalItem : ViewBindingItem<ItemContentVerticalBinding, ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.VERTICAL

    override fun convert(holder: ViewBindingHolder, entity: ContentData, position: Int) {
        if (mBinding.linearListView.childCount != 0) return
        for (index in 1..3) {
            mBinding.linearListView.addView(
                LayoutInflater.from(mBinding.linearListView.context)
                    .inflate(R.layout.item_image, mBinding.linearListView, false)
            )
        }
    }
}