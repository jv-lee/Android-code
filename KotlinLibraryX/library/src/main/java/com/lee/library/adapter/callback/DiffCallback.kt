package com.lee.library.adapter.callback

import androidx.recyclerview.widget.DiffUtil

/**
 * @author jv.lee
 * @date 2020/4/30

 */
class DiffCallback<T> constructor(private val oldData: List<T>,private val newData: List<T>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }

}