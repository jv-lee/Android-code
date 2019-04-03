package com.lee.code.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/31
 */
public class RecyclerView extends ViewGroup {
    /**
     * Y偏移量 内容偏移量
     */
    private int scrollY;
    /**
     * 当前显示的View
     */
    private List<View> viewList;
    /**
     * 当前滑动的y值
     */
    private int currentY;
    /**
     * 总行数
     */
    private int rowCount;
    /**
     * 初始化 第一屏最慢 onlayout()一次
     */
    private boolean needRelayout;

    /**
     * 当前recyclerView宽高
     */
    private int width;
    private int height;

    /**
     * item的高度
     */
    private int[] heights;

    /**
     * 回收类
     */
    Recycler recycler;
    /**
     * 最小滑动距离
     */
    private int touchSlop;
    private Adapter mAdapter;

    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //高度 item
        int h = 0;
        if (mAdapter != null) {
            this.rowCount = mAdapter.getCount();
            heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = mAdapter.getHeight(i);
            }
        }

        //recyclerView 的高度
        //2 假设 item数量少 2item数量多
        int tmpH = sumArray(heights, 0, heights.length);
        h = Math.min(heightSize, tmpH);
        setMeasuredDimension(widthSize, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
    }
    /**
     * 适配器
     */
    interface Adapter{
        /**
         * item的类型
         * @param row
         * @return
         */
        int getItemViewType(int row);

        /**
         * 获取item类型的个数
         * @return
         */
        int getViewTypeCount();

        /**
         * 创建ViewHolder
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        View onCreateViewHolder(int position, View convertView, ViewGroup parent);

        /**
         * 绑定viewHolder 加载数据
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        View onBinderViewHolder(int position, View convertView, ViewGroup parent);

        /**
         * 获取当前高度
         * @param index
         * @return
         */
        int getHeight(int index);

        /**
         * 获取当前item数量
         * @return
         */
        int getCount();
    }

}
