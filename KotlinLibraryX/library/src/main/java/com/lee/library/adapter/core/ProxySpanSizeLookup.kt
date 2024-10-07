package com.lee.library.adapter.core

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * GirdLayoutManager 设置代理SpanSizeLookup
 *
 * @author jv.lee
 * @date 2019/5/20
 */
abstract class ProxySpanSizeLookup(private val recyclerView: RecyclerView) : SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        val adapter = recyclerView.adapter ?: return -1
        val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return -1
        val itemViewType = adapter.getItemViewType(position)
        if (itemViewType == ViewTypeSpec.HEADER || itemViewType == ViewTypeSpec.FOOTER) {
            return layoutManager.spanCount
        }
        return buildSpanSize(position)
    }

    abstract fun buildSpanSize(position: Int): Int
}