package com.lee.library.adapter.manager

import androidx.collection.SparseArrayCompat
import androidx.collection.forEach
import com.lee.library.adapter.base.BaseViewHolder
import com.lee.library.adapter.base.BaseViewItem

/**
 * [BaseViewAdapter] itemView类型管理器
 * @author jv.lee
 * @date 2021/6/15
 */
class ViewItemManager<T> {

    /**
     * android独有高效hashMap
     * key：viewType
     * value: LeeViewItem
     */
    private val styles: SparseArrayCompat<BaseViewItem<T>> = SparseArrayCompat()

    /**
     * 获取所有item样式的总数
     *
     * @return 返回item总数
     */
    fun getItemViewStylesCount() = styles.size()

    /**
     * 加入新的样式 （每次加入放置末尾）
     *
     * @param item item样式
     * 所以第一个样式就是第一个加入的 所以key为0 以此类推不可以添加顺序 和 样式定义的类型不一致
     */
    fun addStyles(item: BaseViewItem<T>?) {
        if (item != null) {
            styles.put(styles.size(), item)
        }
    }

    /**
     * 根据一个数据源和位置返回某item类型的viewType(从styles获取key)
     *
     * @param entity   数据源
     * @param position 下标
     * @return 样式类型
     */
    fun getItemViewType(entity: T, position: Int): Int {
        //样式倒叙循环 ，避免增删集合抛出异常

        //样式倒叙循环 ，避免增删集合抛出异常
        for (i in styles.size() - 1 downTo 0) {
            //比如第1个位置(索引0) ,第一类item样式
            val item = styles.valueAt(i)
            //是否为当前样式显示,由外面实现
            if (item.isItemView(entity, position)) {
                //获得集合key,viewType
                return styles.keyAt(i)
            }
        }
        throw IllegalArgumentException("位置：$position,该item没有匹配的LeeViewItem类型")
    }

    /**
     * 根据显示的viewType 返回LeeViewItem对象
     *
     * @param viewType 布局类型
     * @return LeeViewItem对象
     */
    fun getViewItem(viewType: Int) = styles[viewType]

    /**
     * 视图与数据源绑定显示
     *
     * @param holder   viewHolder
     * @param entity   数据源
     * @param position 当前条目下标
     */
    fun convert(holder: BaseViewHolder, entity: T, position: Int) {
        for (i in 0 until styles.size()) {
            val item = styles.valueAt(i)
            if (item.isItemView(entity, position)) {
                item.convert(holder, entity, position)
                return
            }
        }
        throw java.lang.IllegalArgumentException("位置：$position,该item没有匹配的LeeViewItem类型")
    }

    fun viewRecycled(holder: BaseViewHolder) {
        val itemViewType = holder.itemViewType
        styles.forEach { key, value ->
            if (itemViewType == key) {
                value.viewRecycled(holder)
                return
            }
        }
    }

}