package com.lee.library.adapter.item;

import com.lee.library.R;
import com.lee.library.adapter.listener.LeeViewItem;

public abstract class BaseFooterItem<T> implements LeeViewItem<T> {
    @Override
    public int getItemLayout() {
        return R.layout.item_footer;
    }

    @Override
    public boolean openClick() {
        return false;
    }


}
