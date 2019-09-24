package jv.lee.scroll.adapter;

import android.widget.TextView;

import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

import jv.lee.scroll.R;

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description
 */
public class SimpleItem implements LeeViewItem<String> {
    @Override
    public int getItemLayout() {
        return R.layout.item_text;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean openShake() {
        return true;
    }

    @Override
    public boolean isItemView(String entity, int position) {
        return position != 10;
    }

    @Override
    public void convert(LeeViewHolder holder, String entity, int position) {
        ((TextView)holder.getView(R.id.tv_text)).setText(entity);
    }
}
