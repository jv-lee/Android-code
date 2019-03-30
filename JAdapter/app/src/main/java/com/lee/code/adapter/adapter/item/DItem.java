package com.lee.code.adapter.adapter.item;

import android.widget.TextView;

import com.lee.code.adapter.R;
import com.lee.code.adapter.bean.Userinfo;
import com.lee.library.holder.LeeViewHolder;
import com.lee.library.listener.LeeViewItem;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class DItem implements LeeViewItem<Userinfo> {
    @Override
    public int getItemLayout() {
        return R.layout.item_d;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean isItemView(Userinfo entity, int position) {
        return entity.getType() == 4;
    }

    @Override
    public void convert(LeeViewHolder holder, Userinfo entity, int position) {
        TextView textView = holder.getView(R.id.tv_title);
        textView.setText(entity.getAccount());
    }
}
