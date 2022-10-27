package com.lee.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.lee.app.R
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem

/**
 * 信息item
 * @author jv.lee
 * @date 2020/9/7
 */
class ChatItem : BaseViewItem<String> {

    override fun getItemViewAny(context: Context, parent: ViewGroup): Any {
        return LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
    }

    override fun convert(holder: BaseViewHolder, entity: String, position: Int) {
        val textView = holder.getView<TextView>(R.id.tv_content)
        textView?.text = StringBuilder("this is item data -> $position")
    }

}