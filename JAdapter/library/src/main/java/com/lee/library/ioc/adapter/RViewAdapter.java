package com.lee.library.ioc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RViewAdapter<T> extends RecyclerView.Adapter<RViewHolder> {

    private RView.OnItemClickListener<T> mOnItemClickListener; // 条目点击事件监听
    private RView.OnItemLongClickListener<T> mOnItemLongClickListener; // 条目长按事件监听
    private List<T> datas;

    public RViewAdapter(List<T> datas) {
        if (datas == null) this.datas = new ArrayList<>();
        this.datas = datas;
    }

    void setmOnItemClickListener(RView.OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    void setmOnItemLongClickListener(RView.OnItemLongClickListener<T> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public abstract int getLayoutId();

    public abstract void convert(RViewHolder holder, T t);

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = getLayoutId();
        RViewHolder holder = RViewHolder.createViewHolder(viewGroup.getContext(), viewGroup, layoutId);
        setListener(holder);
        return holder;
    }

    private void setListener(final RViewHolder holder) {
        if (holder == null) return;
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != -1) { // 防止连续点击同一条目而且是删除操作
                        mOnItemClickListener.onItemClick(v, datas.get(position), position);
                    }
                }
            }
        });

        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, datas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        convert(holder, datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}
