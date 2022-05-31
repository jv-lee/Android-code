package com.lee.library.adapter.core

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 垂直tab选择item
 * @author jv.lee
 * @date 2021/11/16
 */
abstract class VerticalTabAdapter<T>(val data: MutableList<T>) :
    RecyclerView.Adapter<VerticalTabAdapter.VerticalTabViewHolder>() {

    protected var selectIndex = 0
    private var mItemClickCall: ItemClickCall? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalTabViewHolder {
        val viewHolder = VerticalTabViewHolder(createViewBinding(parent.context, parent))
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.layoutPosition
            if (position == selectIndex) return@setOnClickListener
            mItemClickCall?.itemClick(position)
            selectItem(viewHolder.layoutPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: VerticalTabViewHolder, position: Int) {
        convert(holder, data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class VerticalTabViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB
    }

    abstract fun createViewBinding(context: Context, parent: ViewGroup): ViewBinding

    abstract fun convert(holder: VerticalTabViewHolder, entity: T, position: Int)

    /**
     * 动态选择item
     */
    fun selectItem(position: Int) {
        val oldPosition = selectIndex
        selectIndex = position

        notifyItemChanged(oldPosition)
        notifyItemChanged(selectIndex)
    }

    /**
     * 更新数据并更新ui
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateNotify(data: List<T>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setItemClickCall(itemClickCall: ItemClickCall) {
        mItemClickCall = itemClickCall
    }

    interface ItemClickCall {
        fun itemClick(position: Int)
    }

}