package com.gionee.gnservice.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {

    private List<T> mDataList = new ArrayList<T>();
    private OnItemClickListener<T> mOnItemClickListener;
    private LayoutInflater mInflater;

    public BaseRecyclerViewAdapter(Context context, List<T> dataList) {
        if (dataList != null) {
            mDataList = dataList;
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(getLayoutId(), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        T t = (i >= mDataList.size()) ? null : mDataList.get(i);
        convert(viewHolder, t, i);
        viewHolder.mConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    if (!RepeatClickUtil.canRepeatClick(viewHolder.mConvertView)) {
                        return;
                    }
                    T t = mDataList.get(viewHolder.getPosition());
                    mOnItemClickListener.onItemClick(t, viewHolder.getPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    public abstract int getLayoutId();

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public List<T> getDatas() {
        return mDataList;
    }

    public void setDatas(List<T> datas) {
        PreconditionsUtil.checkNotNull(datas);
        this.mDataList = datas;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        ViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        public View getConvertView() {
            return mConvertView;
        }

        @SuppressWarnings("unchecked")
        public <V extends View> V getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (V) view;
        }
    }

}
