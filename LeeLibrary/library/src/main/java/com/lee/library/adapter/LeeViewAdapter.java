package com.lee.library.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    private OnItemClickListener<T> mOnItemClickListener;
    /**
     * 条目长按事件监听
     */
    private OnItemLongClickListener<T> mOnItemLongClickListener;
    /**
     * 条目子view点击事件
     */
    private OnItemChildView<T> mOnItemChildChange;
    /**
     * 自动加载更多
     * autoLoadMore
     */
    private AutoLoadMoreListener mAutoLoadMoreListener;
    /**
     * 数据源
     */
    private List<T> mData;

    public static final int END_TYPE = 101;

    /**
     * 单样式构造方法
     *
     * @param data 数据源
     */
    public LeeViewAdapter(List<T> data) {
        if (data == null) {
            this.mData = new ArrayList<>();
        }
        this.mData = data;
        itemStyle = new LeeViewItemManager<>();
    }

    /**
     * 多样式构造方法
     *
     * @param data 数据源
     * @param item item类型
     */
    public LeeViewAdapter(List<T> data, LeeViewItem<T> item) {
        this(data);
        //将item类型加入
        addItemStyles(item);
    }

    /**
     * 添加数据
     *
     * @param data 数据源
     */
    public void addData(List<T> data) {
        if (data == null) {
            return;
        }
        this.mData.addAll(data);
    }

    public void updateData(List<T> data) {
        if (data == null) {
            return;
        }
        this.mData = data;
    }

    protected void addData(T data) {
        if (mData == null) {
            return;
        }
        this.mData.add(data);
    }

    public void clertData() {
        if (mData == null) {
            return;
        }
        this.mData.clear();
    }

    public T getItemByPosition(int position) {
        if (mData == null) {
            throw new RuntimeException("LeeViewAdapter getItemByPosition -> mData == null");
        }
        return mData.get(position);
    }

    /**
     * 判断是否有多样式布局
     *
     * @return boolean
     */
    private boolean hasMultiStyle() {
        return itemStyle.getItemViewStylesCount() > 0;
    }

    /**
     * 根据position获取当前Item的布局类型
     *
     * @param position 下标
     * @return 返回下标类型
     */
    @Override
    public int getItemViewType(int position) {
        //如果有多样式，需要判断
        if (hasMultiStyle()) {
            return itemStyle.getItemViewType(mData.get(position), position);
        }
        return super.getItemViewType(position);
    }

    /**
     * 添加多类型样式方法
     *
     * @param item 样式类型
     */
    protected void addItemStyles(LeeViewItem<T> item) {
        itemStyle.addStyles(item);
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
        convert(holder, mData.get(position));
        if (mAutoLoadMoreListener != null) {
            callEnd(position);
        }
    }

    private int loadMoreNum = 5;
    private boolean hasLoadMore = true;

    /**
     * 自动加载数据
     *
     * @param position
     */
    private void callEnd(int position) {
        int current = getItemCount() - position;
        if (current == loadMoreNum) {
            if (hasLoadMore) {
                hasLoadMore = false;
                //回调加载更多
                mAutoLoadMoreListener.autoLoadMore();
            }
        }
    }

    /**
     * 加载完成
     */
    public void loadMoreCompleted() {
        notifyDataSetChanged();
        hasLoadMore = true;
    }

    /**
     * 没有更多了
     */
    public void loadMoreEnd() {
        notifyDataSetChanged();
        hasLoadMore = false;
    }

    public void setLoadMoreNum(int num) {
        this.loadMoreNum = num;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private long lastClickTime = 0;
    private final long QUICK_EVENT_TIME_SPAN = 2000;

    /**
     * 设置点击监听事件
     *
     * @param viewHolder view复用器
     */
    private void setListener(LeeViewHolder viewHolder) {
        //阻塞事件
        if (mOnItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(v -> {
                int position = viewHolder.getLayoutPosition();
                long timeSpan = System.currentTimeMillis() - lastClickTime;
                if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                mOnItemClickListener.onItemClick(v, mData.get(position), position);
            });
        }

        if (mOnItemChildChange != null) {
            mOnItemChildChange.onItemChild(viewHolder, mData);
        }

        if (mOnItemLongClickListener != null) {
            viewHolder.getConvertView().setOnLongClickListener(v -> {
                int position = viewHolder.getLayoutPosition();
                mOnItemLongClickListener.onItemLongClick(v, mData.get(position), position);
                return true;
            });
        }
    }

    private void convert(LeeViewHolder holder, T entity) {
        itemStyle.convert(holder, entity, holder.getAdapterPosition());
    }

    /**
     * 条目点击监听接口
     */
    public interface OnItemClickListener<T> {

        /**
         * 点击事件监听
         * @param view item
         * @param entity 数据
         * @param position 下标
         */
        void onItemClick(View view, T entity, int position);
    }

    /**
     * 返回所有子view
     *
     * @param <T>
     */
    public interface OnItemChildView<T> {
        /**
         * item子view点击事件
         * @param viewHolder 复用
         * @param data 数据
         */
        void onItemChild(LeeViewHolder viewHolder, List<T> data);
    }

    /**
     * 条目长按监听接口
     */
    public interface OnItemLongClickListener<T> {

        /**
         * 长按事件监听
         * @param view item
         * @param entity 数据
         * @param position 下标
         * @return 返回消费事件
         */
        boolean onItemLongClick(View view, T entity, int position);
    }

    /**
     * 自动加载更多监听接口
     */
    public interface AutoLoadMoreListener {
        /**
         * 回调需要加载更多
         */
        void autoLoadMore();
    }

    /**
     * 添加item点击监听接口
     *
     * @param mOnItemClickListener item接口
     */
    public void setOnItemClickListener(OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildView<T> onItemChildView) {
        this.mOnItemChildChange = onItemChildView;
    }

    public void setAutoLoadMoreListener(AutoLoadMoreListener autoLoadMoreListener) {
        this.mAutoLoadMoreListener = autoLoadMoreListener;
    }
}
