package com.lee.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lee.library.R;
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

    private Context context;
    /**
     * 代理适配器 头尾
     */
    private ProxyAdapter proxyAdapter;
    /**
     * 滑动到剩余最后几条数据时开始加载更多
     */
    private int loadMoreNum = 5;
    /**
     * 加载更多开关 true：可加载 反之即反
     * 默认关闭
     */
    private boolean hasLoadMore = false;
    /**
     * 加载更多view显示 true：可加载 反之即反
     */
    private boolean hasShowLoad = true;
    /**
     * 加载更多 view
     */
    private View loadMoreView;
    /**
     * 没有更多view
     */
    private View loadEndView;

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

    private int endResId;
    private int loadResId;

    /**
     * 单样式构造方法
     *
     * @param data 数据源
     */
    public LeeViewAdapter(Context context, List<T> data) {
        this.context = context;
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
    public LeeViewAdapter(Context context, List<T> data, LeeViewItem<T> item) {
        this(context, data);
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

    /**
     * 添加头部尾部代理适配
     *
     * @return
     */
    public ProxyAdapter getProxy() {
        if (proxyAdapter == null) {
            proxyAdapter = new ProxyAdapter(this);
        }
        return proxyAdapter;
    }

    public void addHeader(View view) {
        if (proxyAdapter == null) {
            getProxy();
        }
        proxyAdapter.addHeaderView(view);
    }

    public void addFooter(View view) {
        if (proxyAdapter == null) {
            getProxy();
        }
        proxyAdapter.addFooterView(view);
    }

    public void removeHeader(View view) {
        if (proxyAdapter == null) {
            getProxy();
        }
        proxyAdapter.removeHeaderView(view);
    }

    public void removeFooter(View view) {
        if (proxyAdapter == null) {
            getProxy();
        }
        proxyAdapter.removeFooterView(view);
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
        if (mAutoLoadMoreListener != null && hasLoadMore) {
            callEnd(position);
        }
    }

    /**
     * 自动加载数据
     *
     * @param position
     */
    private void callEnd(int position) {
        int current = getItemCount() - position;
        //回调加载更多 关闭开关
        if (current == loadMoreNum) {
            if (loadMoreView.getVisibility() == View.GONE) {
                loadMoreView.setVisibility(View.VISIBLE);
            }
            hasLoadMore = false;
            mAutoLoadMoreListener.autoLoadMore();
        }
    }

    public void openLoadMore() {
        hasLoadMore = true;
        if (loadMoreView == null) {
            loadMoreView = LayoutInflater.from(context).inflate(loadResId == 0 ? R.layout.lee_item_load : loadResId, new FrameLayout(context), false);
        }
        if (loadMoreView.getVisibility() == View.VISIBLE) {
            loadMoreView.setVisibility(View.GONE);
        }
        addFooter(loadMoreView);
        //设置图片闪烁，给所有item view添加tag
        if (!hasStableIds()) {
            setHasStableIds(true);
        }
    }

    private void closeLoadMore() {
        if (loadMoreView != null) {
            removeFooter(loadMoreView);
        }
    }

    /**
     * 加载完成
     */
    public void loadMoreCompleted() {
        hasLoadMore = true;
        notifyDataSetChanged();
    }

    /**
     * 没有更多了
     */
    public void loadMoreEnd() {
        //添加底部布局
        if (proxyAdapter == null) {
            getProxy();
        }
        closeLoadMore();
        //加载end布局提示加载完毕
        if (loadEndView == null) {
            loadEndView = LayoutInflater.from(context).inflate(endResId == 0 ? R.layout.lee_item_end : endResId, new FrameLayout(context), false);
        }
        proxyAdapter.addFooterView(loadEndView);
        hasLoadMore = false;
        notifyDataSetChanged();
    }

    /**
     * 刷新后 调用重置加载更多
     */
    public void reloadMoreEnd() {
        if (proxyAdapter == null) {
            getProxy();
        }
        openLoadMore();
        proxyAdapter.removeFooterView(loadEndView);
    }

    /**
     * 设置没有更多数据 layout id
     *
     * @param resId
     */
    public void setEndResId(int resId) {
        this.endResId = resId;
    }

    /**
     * 设置加载更多view 布局id
     *
     * @param resId
     */
    public void setLoadResId(int resId) {
        this.loadResId = resId;
    }

    /**
     * 设置加载更多最低阈值
     *
     * @param num
     */
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
     * 获取当前数据下标
     *
     * @param viewHolder
     * @return
     */
    private int getPosition(LeeViewHolder viewHolder) {
        return proxyAdapter == null ? viewHolder.getLayoutPosition() : viewHolder.getLayoutPosition() - proxyAdapter.getHeaderCount();
    }

    /**
     * 设置点击监听事件
     *
     * @param viewHolder view复用器
     */
    private void setListener(LeeViewHolder viewHolder) {
        //阻塞事件
        if (mOnItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(v -> {
                int position = getPosition(viewHolder);
                long timeSpan = System.currentTimeMillis() - lastClickTime;
                if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                if (position >= 0) {
                    mOnItemClickListener.onItemClick(v, mData.get(position), position);
                }
            });
        }

        if (mOnItemChildChange != null) {
            int position = getPosition(viewHolder);
            if (position >= 0) {
                mOnItemChildChange.onItemChild(viewHolder, mData.get(position), position);
            }
        }

        if (mOnItemLongClickListener != null) {
            viewHolder.getConvertView().setOnLongClickListener(v -> {
                int position = getPosition(viewHolder);
                if (position >= 0) {
                    mOnItemLongClickListener.onItemLongClick(v, mData.get(position), position);
                }
                return true;
            });
        }
    }

    private void convert(LeeViewHolder holder, T entity) {
        itemStyle.convert(holder, entity, holder.getLayoutPosition());
    }

    /**
     * 条目点击监听接口
     */
    public interface OnItemClickListener<T> {

        /**
         * 点击事件监听
         *
         * @param view     item
         * @param entity   数据
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
         *
         * @param viewHolder 复用
         * @param entity     数据
         * @param position   下标
         */
        void onItemChild(LeeViewHolder viewHolder, T entity, int position);
    }

    /**
     * 条目长按监听接口
     */
    public interface OnItemLongClickListener<T> {

        /**
         * 长按事件监听
         *
         * @param view     item
         * @param entity   数据
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
