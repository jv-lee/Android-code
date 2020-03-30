package com.lee.library.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lee.library.R;
import com.lee.library.adapter.listener.LeeViewItem;
import com.lee.library.adapter.manager.LeeViewItemManager;
import com.lee.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
     * 加载更多 view
     */
    private View loadMoreLayout;
    private View loadMoreView;
    private View loadEndView;
    private View loadErrorView;

    private final int STATUS_INIT = 0;
    private final int STATUS_MORE = 1;
    private final int STATUS_END = 2;
    private final int STATUS_ERROR = 3;

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

    /**
     * 加载布局id
     */
    private int loadResId;

    /**
     * 点击防抖结束时间
     */
    private long lastClickTime = 0;

    /**
     * 点击防抖动时间阈值
     */
    private final long QUICK_EVENT_TIME_SPAN = 1000;

    /**
     * 子view点击id集合
     */
    private List<Integer> childClickIds = new ArrayList<>();

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
     * 添加数据源（分页操作）
     *
     * @param data 数据源
     */
    public void addData(List<T> data) {
        if (data == null) {
            return;
        }
        this.mData.addAll(data);
    }

    /**
     * 刷新数据源 (初始页操作)
     *
     * @param data 数据源
     */
    public void updateData(List<T> data) {
        if (data == null) {
            return;
        }
        if (data.size() != 0) {
            //清空数据集合 通知数据源发生变更
            this.mData.clear();
            notifyDataSetChanged();
        }
        this.mData.addAll(data);

    }

    protected void addData(T data) {
        if (mData == null) {
            return;
        }
        this.mData.add(data);
    }

    public void clearData() {
        if (mData == null) {
            return;
        }
        this.mData.clear();
    }

    public List<T> getData() {
        if (mData == null) {
            throw new RuntimeException("LeeViewAdapter getData - > mData == null");
        }
        return mData;
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

    private SparseArray<LeeViewHolder> viewHolders = new SparseArray<>();

    @NonNull
    @Override
    public LeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据布局的类型 创建不同的ViewHolder
        LeeViewItem item = itemStyle.getLeeViewItem(viewType);
        int layoutId = item.getItemLayout();
        LeeViewHolder viewHolder = LeeViewHolder.createViewHolder(parent.getContext(), parent, layoutId);

        //点击的监听
        if (item.openClick()) {
            setListener(viewHolder, item.openShake());
            //子view监听
            setChildListener(viewHolder, item.openShake());
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
            updateStatus(STATUS_MORE);
            hasLoadMore = false;
            //防止更新过快导致 RecyclerView 还处于锁定状态 就直接更新数据
            ValueAnimator value = ValueAnimator.ofInt(0, 1);
            value.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAutoLoadMoreListener.autoLoadMore();
                }
            });
            value.setDuration(50);
            value.start();
        }
    }

    private void updateStatus(int status) {
        if (loadMoreView == null || loadEndView == null || loadErrorView == null) {
            return;
        }
        loadMoreView.setVisibility(View.GONE);
        loadErrorView.setVisibility(View.GONE);
        loadEndView.setVisibility(View.GONE);
        switch (status) {
            case STATUS_MORE:
                loadMoreView.setVisibility(View.VISIBLE);
                break;
            case STATUS_END:
                loadEndView.setVisibility(View.VISIBLE);
                break;
            case STATUS_ERROR:
                loadErrorView.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    public void openLoadMore() {
        hasLoadMore = true;
        if (loadMoreLayout == null) {
            loadMoreLayout = LayoutInflater.from(context).inflate(loadResId == 0 ? R.layout.lee_item_load : loadResId, new FrameLayout(context), false);
            loadMoreView = loadMoreLayout.findViewById(R.id.view_loadMore);
            loadEndView = loadMoreLayout.findViewById(R.id.view_loadEnd);
            loadErrorView = loadMoreLayout.findViewById(R.id.view_loadError);
            addFooter(loadMoreLayout);
        }
        updateStatus(STATUS_INIT);
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
        updateStatus(STATUS_END);
        hasLoadMore = false;
        notifyDataSetChanged();
    }

    /**
     * 加载完成
     */
    public void loadMoreCompleted(int count) {
        hasLoadMore = true;
        int startIndex = mData.size() - count;
        notifyItemRangeInserted(startIndex, count);
    }

    /**
     * 没有更多了
     */
    public void loadMoreEnd(int count) {
        //添加底部布局
        if (proxyAdapter == null) {
            getProxy();
        }
        updateStatus(STATUS_END);
        hasLoadMore = false;
        int startIndex = mData.size() - count;
        notifyItemRangeInserted(startIndex, count);
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
     * num = 5 则为 20-5 = 滑动到15项的时候加载
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
     * 设置子点击监听事件
     *
     * @param viewHolder view复用器
     */
    private void setChildListener(LeeViewHolder viewHolder, boolean shake) {
        if (mOnItemChildChange != null) {
            for (Integer childClickId : childClickIds) {
                View view = viewHolder.getConvertView().findViewById(childClickId);
                if (view == null) {
                    continue;
                }
                view.setOnClickListener(v -> {
                    if (shake) {
                        long timeSpan = System.currentTimeMillis() - lastClickTime;
                        if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                            return;
                        }
                        lastClickTime = System.currentTimeMillis();
                    }
                    int position = getPosition(viewHolder);
                    if (position >= 0) {
                        mOnItemChildChange.onItemChild(view, mData.get(position), position);
                    }
                });
            }
        }
    }

    /**
     * 设置点击监听事件
     *
     * @param viewHolder view复用器
     */
    private void setListener(LeeViewHolder viewHolder, boolean shake) {
        //阻塞事件
        if (mOnItemClickListener != null) {
            viewHolder.getConvertView().setOnClickListener(v -> {
                int position = getPosition(viewHolder);
                if (shake) {
                    long timeSpan = System.currentTimeMillis() - lastClickTime;
                    if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                        return;
                    }
                    lastClickTime = System.currentTimeMillis();
                }
                if (position >= 0) {
                    mOnItemClickListener.onItemClick(v, mData.get(position), position);
                }
            });
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
         * @param view     控件
         * @param entity   数据
         * @param position 下标
         */
        void onItemChild(View view, T entity, int position);
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

    public void setOnItemChildClickListener(OnItemChildView<T> onItemChildView, Integer... childClickIds) {
        this.childClickIds = Arrays.asList(childClickIds);
        this.mOnItemChildChange = onItemChildView;
    }

    public void setAutoLoadMoreListener(AutoLoadMoreListener autoLoadMoreListener) {
        this.mAutoLoadMoreListener = autoLoadMoreListener;
    }

}
