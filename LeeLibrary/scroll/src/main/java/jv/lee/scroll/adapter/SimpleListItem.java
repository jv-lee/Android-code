package jv.lee.scroll.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

import java.util.ArrayList;
import java.util.List;

import jv.lee.scroll.R;
import jv.lee.scroll.widget.RecyclerViewEx;

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description
 */
public class SimpleListItem implements LeeViewItem<String> {
    @Override
    public int getItemLayout() {
        return R.layout.item_list;
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
        return position == 10;
    }

    @Override
    public void convert(LeeViewHolder holder, String entity, int position) {
        RecyclerViewEx rvItemContainer = holder.getView(R.id.rv_item_container);
        rvItemContainer.setLayoutManager(new LinearLayoutManager(holder.getConvertView().getContext()));

        List<String> array = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            array.add("RecyclerViewEx item list data index:" + i);
        }
        rvItemContainer.setAdapter(new SimpleAdapter(holder.getConvertView().getContext(),array));
    }
}
