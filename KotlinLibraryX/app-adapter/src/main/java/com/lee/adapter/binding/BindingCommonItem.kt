package com.lee.adapter.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.adapter.databinding.ItemContentCommonBinding
import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.ViewType
import com.lee.library.adapter.binding.ViewBindingHolder
import com.lee.library.adapter.item.ViewBindingItem

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class BindingCommonItem : ViewBindingItem<ItemContentCommonBinding,ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.COMMON

    override fun convert(holder: ViewBindingHolder, entity: ContentData, position: Int) {
        mBinding.tvContent.text = "COMMON VIEW_TYPE -> page in - ${entity.id} , index in - $position"
    }
}