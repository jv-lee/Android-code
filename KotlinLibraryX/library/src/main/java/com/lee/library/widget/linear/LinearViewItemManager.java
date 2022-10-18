package com.lee.library.widget.linear;


import android.view.View;

import androidx.collection.SparseArrayCompat;

/**
 * 多类型、多样式item管理器
 * @author jv.lee
 * @date 2020/9/7
 */
public class LinearViewItemManager<T> {

    /**
     * android独有高效hashMap
     * key：viewType
     * value: LeeViewItem
     */
    private final SparseArrayCompat<LinearViewItem<T>> styles = new SparseArrayCompat<>();

    /**
     * 获取所有item样式的总数
     *
     * @return 返回item总数
     */
    public int getItemViewStylesCount() {
        return styles.size();
    }

    /**
     * 加入新的样式 （每次加入放置末尾）
     *
     * @param item item样式
     *             所以第一个样式就是第一个加入的 所以key为0 以此类推不可以添加顺序 和 样式定义的类型不一致
     */
    public void addStyles(LinearViewItem<T> item) {
        if (item != null) {
            styles.put(styles.size(), item);
        }
    }

    /**
     * 根据一个数据源和位置返回某item类型的viewType(从styles获取key)
     *
     * @param entity   数据源
     * @param position 下标
     * @return 样式类型
     */
    public int getItemViewType(T entity, int position) {
        //样式倒叙循环 ，避免增删集合抛出异常
        for (int i = styles.size() - 1; i >= 0; i--) {
            //比如第1个位置(索引0) ,第一类item样式
            LinearViewItem<T> item = styles.valueAt(i);
            //是否为当前样式显示,由外面实现
            if (item.isItemView(entity, position)) {
                //获得集合key,viewType
                return styles.keyAt(i);
            }
        }
        throw new IllegalArgumentException("位置：" + position + ",该item没有匹配的LeeViewItem类型");
    }

    /**
     * 根据显示的viewType 返回LeeViewItem对象
     *
     * @param viewType 布局类型
     * @return LeeViewItem对象
     */
    public LinearViewItem getViewItem(int viewType) {
        return styles.get(viewType);
    }

    /**
     * 视图与数据源绑定显示
     *
     * @param view   itemView
     * @param entity   数据源
     * @param position 当前条目下标
     */
    public void convert(View view, T entity, int position) {
        for (int i = 0; i < styles.size(); i++) {
            LinearViewItem<T> item = styles.valueAt(i);

            if (item.isItemView(entity, position)) {
                item.convert(view, entity, position);
                return;
            }
        }
        throw new IllegalArgumentException("位置：" + position + ",该item没有匹配的LeeViewItem类型");
    }

}
