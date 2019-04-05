package com.lee.library.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lee.library.adapter.listener.LeeViewItem;
import com.lee.library.adapter.manager.LeeViewItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 * 封装的RecyclerViewAdapter
 */
public class LeeViewAdapter<T> extends RecyclerView.Adapter<LeeViewHolder> {

    /**
     * item类型管理器
     */
    private LeeViewItemManager<T> itemStyle;
    /**
     * 条目点击事件监听
     */
    private LeeRecyclerView.OnItemClickListener<T> mOnItemClickListener;
    /**
     * 条目长按事件监听
     */
    private LeeRecyclerView.OnItemLongClickListener<T> mOnItemLongClickListener;
    /**
     * 数据源
     */
    private List<T> datas;

    /**
     * 单样式构造方法
     * @param datas 数据源
     */
     public LeeViewAdapter(List<T> datas) {
        if (datas == null) {
            this.datas = new ArrayList<>();
        }
        this.datas = datas;
        itemStyle = new LeeViewItemManager<>();
    }

    /**
     * 多样式构造方法
     * @param datas 数据源
     * @param item item类型
     */
    public LeeViewAdapter(List<T> datas, LeeViewItem<T> item) {
        this(datas);
        //将item类型加入
        addItemStyles(item);
    }

    /**
     * 添加数据
     * @param datas 数据源
     */
    public void addDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        this.datas.addAll(datas);notifyDataSetChanged();
    }

    public void updateDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * 判断是否有多样式布局
     * @return boolean
     */
    private boolean hasMultiStyle(){
        return itemStyle.getItemViewStylesCount() > 0;
    }

    /**
     * 根据position获取当前Item的布局类型
     * @param position 下标
     * @return 返回下标类型
     */
    @Override
    public int getItemViewType(int position) {
        //如果有多样式，需要判断
        if (hasMultiStyle()) {
            return itemStyle.getItemViewType(datas.get(position),position);
        }
        return super.getItemViewType(position);
    }

    /**
     * 添加多类型样式方法
     * @param item 样式类型
     */
    public void addItemStyles(LeeViewItem<T> item) {
        itemStyle.addStyles(item);
    }



    /**
     * 添加item点击监听接口
     * @param mOnItemClickListener item接口
     */
    void setmOnItemClickListener(LeeRecyclerView.OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    void setmOnItemLongClickListener(LeeRecyclerView.OnItemLongClickListener<T> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    @NonNull
    @Override
    public LeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据布局的类型 创建不同的ViewHolder
        LeeViewItem item = itemStyle.getLeeViewItem(viewType);
        int layoutId = item.getItemLayout();
        LeeViewHolder viewHolder = LeeViewHolder.createViewHolder(parent.getContext(), parent, layoutId);

        //点击的监听
        if (item.openClick()) {
            setListener(viewHolder);
        }
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull LeeViewHolder holder, int position) {
        convert(holder,datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 :datas.size();
    }

    private long lastClickTime = 0;
    private final long QUICK_EVENT_TIME_SPAN = 2000;

    /**
     * 设置点击监听事件
     * @param viewHolder view复用器
     */
    private void setListener(LeeViewHolder viewHolder) {
        //阻塞事件
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getLayoutPosition();
                long timeSpan = System.currentTimeMillis() - lastClickTime;
                if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();

                mOnItemClickListener.onItemClick(v,datas.get(position),position);
            }
        });
        viewHolder.getConvertView().setOnLongClickListener(v ->{
            if (mOnItemLongClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(v,datas.get(position),position);
            }
            return true;
        });
    }

    private void convert(LeeViewHolder holder, T entity) {
        itemStyle.convert(holder, entity, holder.getAdapterPosition());
    }
}
