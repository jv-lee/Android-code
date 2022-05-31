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
 * @author jv.lee
 * @date 2020/11/25

 */
class BindingCommonItem : ViewBindingItem<ContentData>() {

    override fun isItemView(entity: ContentData, position: Int) = entity.type == ViewType.COMMON

    override fun getItemViewBinding(context: Context, parent: ViewGroup): ViewBinding {
        return ItemContentCommonBinding.inflate(LayoutInflater.from(context), parent, false)
    }

    override fun convert(holder: ViewBindingHolder, entity: ContentData, position: Int) {
        holder.getViewBinding<ItemContentCommonBinding>().run {
            tvContent.text = "COMMON VIEW_TYPE -> page in - ${entity.id} , index in - $position"
        }
    }


}