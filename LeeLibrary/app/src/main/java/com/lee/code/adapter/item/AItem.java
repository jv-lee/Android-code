package com.lee.code.adapter.item;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.code.R;
import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;


/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class AItem implements LeeViewItem<UserInfo> {
    @Override
    public int getItemLayout() {
        return R.layout.item_a;
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
        return entity.getType() == 1;
    }

    @Override
    public void convert(LeeViewHolder holder, UserInfo entity, int position) {
        TextView textView = holder.getView(R.id.tv_title);
        textView.setText(entity.getAccount());
        LinearLayout llContainer = holder.getView(R.id.ll_container);

    }
}
