package com.lee.api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.viewbinding.ViewBinding;

import com.lee.api.databinding.ItemContentBinding;
import com.lee.library.adapter.LeeViewAdapter;
import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;
import com.lee.library.adapter.manager.LeeViewItemManager;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 封装的RecyclerViewAdapter
 */
public class ViewBindingAdapter<T> extends LeeViewAdapter<T> {

    /**
     * item类型管理器
     */
    private LeeViewItemManager<T> itemStyle;

    public ViewBindingAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public ViewBindingAdapter(Context context, List<T> data, LeeViewItem<T> item) {
        super(context, data, item);
    }

    @NonNull
    @Override
    public LeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据布局的类型 创建不同的ViewHolder
        LeeViewItem item = itemStyle.getLeeViewItem(viewType);
        int layoutId = item.getItemLayout();

        ViewBinding binding = ItemContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        LeeViewHolder viewHolder = new LeeViewHolder(binding.getRoot());

        //点击的监听
        if (item.openClick()) {
            setListener(viewHolder, item.openShake());
            //子view监听
            setChildListener(viewHolder, item.openShake());
        }
        return viewHolder;
    }
}
