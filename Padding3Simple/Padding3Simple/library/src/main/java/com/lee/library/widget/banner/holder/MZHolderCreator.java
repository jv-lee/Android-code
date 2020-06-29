package com.lee.library.widget.banner.holder;

/**
 *
 * @author Administrator
 * @date 2017/8/15
 */
public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
