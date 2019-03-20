package com.lee.code.ioc;

import android.widget.TextView;

import com.lee.library.ioc.adapter.RViewAdapter;
import com.lee.library.ioc.adapter.RViewHolder;

import java.util.List;

public class RvAdapter extends RViewAdapter<String> {

    public RvAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_item;
    }

    @Override
    public void convert(RViewHolder holder, String s) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(s);
    }
}
