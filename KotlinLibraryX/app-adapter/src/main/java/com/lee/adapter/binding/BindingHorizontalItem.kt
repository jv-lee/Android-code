package com.lee.adapter.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.adapter.R
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
class BindingHorizontalItem : ViewBindingItem<ItemContentHorizontalBinding,ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.HORIZONTAL

    override fun convert(holder: ViewBindingHolder, entity: ContentData, position: Int) {
        if (mBinding.linearListView.childCount != 0) return
        for (index in 1..6) {
            mBinding.linearListView.addView(
                LayoutInflater.from(mBinding.linearListView.context)
                    .inflate(R.layout.item_image, mBinding.linearListView, false)
            )
        }
    }
}