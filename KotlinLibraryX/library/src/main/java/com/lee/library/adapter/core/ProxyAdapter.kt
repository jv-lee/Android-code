package com.lee.library.adapter.core

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView 代理适配器，可通过该适配器添加header、footer
 *
 * @author jv.lee
 * @date 2019/5/20
 */
class ProxyAdapter(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mHeaderViews: ArrayList<View> = arrayListOf()
    private val mFooterViews: ArrayList<View> = arrayListOf()

    init {
        // 给所有item view添加tag 防止图片闪烁
        adapter.setHasStableIds(adapter.hasStableIds())
        setHasStableIds(adapter.hasStableIds())
        changeSpanSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val type = getType(viewType)
        val value = getValue(viewType)
        return when (type) {
            ViewTypeSpec.HEADER -> {
                FixedViewHolder(mHeaderViews[value])
            }

            ViewTypeSpec.FOOTER -> {
                FixedViewHolder(mFooterViews[value])
            }

            else -> {
                adapter.onCreateViewHolder(parent, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FixedViewHolder) {
            holder.onBind()
        } else {
            val adjPosition = position - mHeaderViews.size
            adapter.onBindViewHolder(holder, adjPosition)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapter.unregisterAdapterDataObserver(observer)
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        adapter.registerAdapterDataObserver(observer)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is FixedViewHolder) {
            return
        }
        adapter.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder is FixedViewHolder) {
            return
        }
        adapter.onViewAttachedToWindow(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        if (holder is FixedViewHolder) {
            return false
        }
        return adapter.onFailedToRecycleView(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is FixedViewHolder) {
            return
        }
        adapter.onViewRecycled(holder)
    }

    override fun getItemId(position: Int): Long {
        val adjPosition = position - mHeaderViews.size
        if (adjPosition >= 0 && adjPosition < adapter.itemCount) {
            return adapter.getItemId(adjPosition)
        }
        return RecyclerView.NO_ID
    }

    override fun getItemCount() = mHeaderViews.size + mFooterViews.size + adapter.itemCount

    override fun getItemViewType(position: Int): Int {
        val numHeaderView = mHeaderViews.size

        if (position < numHeaderView) {
            return makeItemViewTypeSpec(position, ViewTypeSpec.HEADER)
        }

        val adjPosition = position - numHeaderView
        val itemCount = adapter.itemCount
        if (adjPosition >= itemCount) {
            return makeItemViewTypeSpec(adjPosition - itemCount, ViewTypeSpec.FOOTER)
        }

        val itemViewType = adapter.getItemViewType(adjPosition)
        if (itemViewType < 0 || itemViewType > (1 shl TYPE_SHIFT) - 1) {
            throw IllegalArgumentException(
                "Invalid item view type: RecyclerView.Adapter.getItemViewType return $itemViewType"
            )
        }
        return itemViewType
    }

    fun addHeaderView(view: View) {
        if (!mHeaderViews.contains(view)) {
            mHeaderViews.add(view)
        }
    }

    fun removeHeaderView(view: View) {
        mHeaderViews.remove(view)
    }

    fun addFooterView(view: View) {
        if (!mFooterViews.contains(view)) {
            mFooterViews.add(view)
        }
    }

    fun addFooterViewAtTop(view: View) {
        if (!mFooterViews.contains(view)) {
            mFooterViews.add(0, view)
        }
    }

    fun removeFooterView(view: View) {
        mFooterViews.remove(view)
    }

    fun getHeaderCount() = mHeaderViews.size

    fun getFooterCount() = mFooterViews.size

    private fun isFixedViewType(viewType: Int): Boolean {
        val type = getType(viewType)
        return type == ViewTypeSpec.HEADER || type == ViewTypeSpec.FOOTER
    }

    private fun changeSpanSize() {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : ProxySpanSizeLookup(recyclerView) {
                override fun buildSpanSize(position: Int): Int = 1
            }
        }
    }

    private class FixedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            setIsRecyclable(false)
        }

        fun onBind() {}
    }

    private companion object {
        const val TYPE_SHIFT = 30
        const val TYPE_MASK = 0x3 shl TYPE_SHIFT

        fun makeItemViewTypeSpec(
            @androidx.annotation.IntRange(
                from = 0,
                to = ((1 shl TYPE_SHIFT) - 1).toLong()
            ) value: Int, @ViewTypeSpec type: Int
        ): Int {
            return (value and TYPE_MASK.inv()) or (type and TYPE_MASK);
        }

        fun getType(viewType: Int): Int {
            return (viewType and TYPE_MASK)
        }

        fun getValue(viewType: Int): Int {
            return (viewType and TYPE_MASK.inv())
        }
    }
}

@IntDef(ViewTypeSpec.UNSPECIFIED, ViewTypeSpec.HEADER, ViewTypeSpec.FOOTER)
@Retention(AnnotationRetention.SOURCE)
annotation class ViewTypeSpec {
    companion object {
        const val UNSPECIFIED = 0
        const val HEADER = 1 shl 30
        const val FOOTER = 2 shl 30
    }
}