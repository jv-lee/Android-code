package com.lee.library.adapter.item;

import com.lee.library.R;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

public class FooterItem implements LeeViewItem {
    @Override
    public int getItemLayout() {
        return R.layout.item_footer;
    }

    @Override
    public boolean openClick() {
        return false;
    }

    @Override
    public boolean isItemView(Object entity, int position) {
        return false;
    }

    @Override
    public void convert(LeeViewHolder holder, Object entity, int position) {

    }

}
