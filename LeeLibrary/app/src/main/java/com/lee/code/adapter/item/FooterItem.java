package com.lee.code.adapter.item;

import com.lee.code.R;
import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

public class FooterItem implements LeeViewItem<UserInfo> {
    @Override
    public int getItemLayout() {
        return R.layout.item_footer;
    }

    @Override
    public boolean openClick() {
        return false;
    }

    @Override
    public boolean isItemView(UserInfo entity, int position) {
        return entity.getType() == 101;
    }

    @Override
    public void convert(LeeViewHolder holder, UserInfo entity, int position) {

    }
}
