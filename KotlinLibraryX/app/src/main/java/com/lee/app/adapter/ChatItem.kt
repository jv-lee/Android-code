package com.lee.app.adapter

import android.widget.TextView
import com.lee.app.R
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
class ChatItem : LeeViewItem<String> {
    override fun getItemLayout(): Int {
        return R.layout.item_chat
    }

    override fun openClick(): Boolean {
        return true
    }

    override fun openShake(): Boolean {
        return true
    }

    override fun openRecycler(): Boolean {
        return false
    }

    override fun isItemView(entity: String?, position: Int): Boolean {
        return entity != null
    }

    override fun convert(holder: LeeViewHolder?, entity: String?, position: Int) {
        val textView = holder?.getView<TextView>(R.id.tv_content)
        textView?.text = "this is item data -> $position"
    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: String?, position: Int) {
    }

}