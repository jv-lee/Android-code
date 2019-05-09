package com.lee.code.adapter.item;

import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.item.BaseFooterItem;

public class FooterItem extends BaseFooterItem<UserInfo> {

    @Override
    public boolean isItemView(UserInfo entity, int position) {
        return entity.getType() == 101;
    }

    @Override
    public void convert(LeeViewHolder holder, UserInfo entity, int position) {

    }
}
