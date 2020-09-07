package com.lee.app.adapter

import android.widget.EditText
import com.lee.app.R
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description
 */
class FormItem : LeeViewItem<String> {
    override fun getItemLayout(): Int {
        return R.layout.item_form
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
        val editText = holder?.getView<EditText>(R.id.et_input)
        editText?.setText("this is content position $position")
    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: String?, position: Int) {
    }
}