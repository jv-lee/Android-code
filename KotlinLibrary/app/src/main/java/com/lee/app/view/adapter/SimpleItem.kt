package com.lee.app.view.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.lee.app.R
import com.lee.library.adapter.LeeViewHolder
import com.lee.library.adapter.listener.LeeViewItem

/**
 * @author jv.lee
 * @date 2019/9/17.
 * @description
 */
class SimpleItem : LeeViewItem<Int> {


    override fun getItemLayout(): Int {
        return R.layout.item_text
    }

    override fun openClick(): Boolean {
        return true
    }

    override fun openShake(): Boolean {
        return true
    }

    override fun isItemView(entity: Int?, position: Int): Boolean {
        return entity != null
    }

    override fun convert(holder: LeeViewHolder?, entity: Int?, position: Int) {
        (holder?.getView<View>(R.id.tv_text) as TextView).text =
            "this is item text position $entity"
        if (position % 2 == 0) {
            holder.convertView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.convertView.context,
                    android.R.color.holo_blue_bright
                )
            )
        } else {
            holder.convertView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.convertView.context,
                    android.R.color.holo_orange_light
                )
            )
        }
    }

    override fun openRecycler(): Boolean {
        return false
    }

    override fun viewRecycled(holder: LeeViewHolder?, entity: Int?, position: Int) {
    }


}