package com.lee.code.adapter.item;

import android.widget.TextView;

import com.lee.code.R;
import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class DItem implements LeeViewItem<UserInfo> {
    @Override
    public int getItemLayout() {
        return R.layout.item_d;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean openShake() {
        return false;
    }

    @Override
    public boolean isItemView(UserInfo entity, int position) {
        return entity.getType() == 4;
    }

    @Override
    public void convert(LeeViewHolder holder, UserInfo entity, int position) {
        TextView textView = holder.getView(R.id.tv_title);
        textView.setText(entity.getAccount());
    }
}
