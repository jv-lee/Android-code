package com.lee.code.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
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
     * 第几行
     */
    private int firstRow;
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


    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
        this.viewList = new ArrayList<>();
        this.needRelayout = true;
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
        if (needRelayout || changed) {
            needRelayout = false;

            viewList.clear();
            removeAllViews();
            if (mAdapter != null) {
                //设置每一个itemView
                width = r - l;
                height = b - t;
                int left = 0, top = 0, right, bottom;
                for (int i = 0; i < rowCount && top < height; i++) {
                    right = width;
                    bottom = top + heights[i];

                    //生成一个itemView
                    View view = makeAndStep(i, left, top, right, bottom);
                    viewList.add(view);
                    top = bottom;
                }
            }

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                currentY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int y2 = Math.abs(currentY - (int) event.getRawY());
                if (y2 > touchSlop) {
                    intercept = true;
                }
            }
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
//                移动的距离   y方向
                int y2 = (int) event.getRawY();
//         //            上滑正  下滑负
                int diffY = currentY - y2;
//                画布移动  并不影响子控件的位置
                scrollBy(0, diffY);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理极限边界值
     * @param scrollY
     * @return
     */
    private int scrollBounds(int scrollY) {
        //上滑
        if (scrollY > 0) {
            scrollY = Math.min(scrollY, sumArray(heights, firstRow, heights.length - firstRow) - height);
        }else{
            //在极限值时 取0，非极限值取scrollY
            scrollY = Math.max(scrollY,-sumArray(heights,0,firstRow));
        }
        return scrollY;
    }

    @Override
    public void scrollBy(int x, int y) {
        //scrollY表示 第一个可见item的左上定点 距离屏幕左上定点的距离
        scrollY += y;
        scrollY = scrollBounds(scrollY);
        //上滑正 下滑负 边界值  使用while判断 用户高速滑动时处理view回收删除
        if (scrollY > 0) {
            //1.上滑移除
            while (scrollY > heights[firstRow]) {
                //1.上滑移除  4.下滑加载
                removeView(viewList.remove(0));
                scrollY -= heights[0];
                firstRow++;
            }

            //2.上滑加载
            while (getFillHeight() < height) {
                int addLast = firstRow + viewList.size();
                View view = obtainView(addLast, width, heights[addLast]);
                viewList.add(viewList.size(), view);
            }
        } else if (scrollY < 0) {

            //3.下滑移除
            while (sumArray(heights, firstRow, viewList.size()) - scrollY - heights[firstRow + viewList.size() - 1] >=height) {
                removeView(viewList.remove(viewList.size()-1));
            }

            //4.下滑加载
            while (scrollY < 0) {
                int firstAddRow = firstRow - 1;
                View view = obtainView(firstAddRow, width, heights[firstAddRow]);
                viewList.add(0, view);
                firstRow--;
                scrollY += heights[firstRow + 1];
            }

        } else {
        }
        repositionViews();
    }

    private void repositionViews() {
        int left=0,top,right,bottom,i;
        top = -scrollY;
        right = width;
        i = firstRow;
        for (View view : viewList) {
            bottom = top + heights[i++];
            view.layout(left, top, right, bottom);
            top = bottom;
        }
    }


    /**
     * 数据的高度 - scrollY
     *
     * @return
     */
    private int getFillHeight() {
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        //滑动隐藏 删除view  放入回收池
        int key = (int) view.getTag(R.id.tag_type_view);
        recycler.put(view, key);
    }

    private View makeAndStep(int row, int left, int top, int right, int bottom) {
        View view = obtainView(row, right - left, bottom - top);
        view.layout(left, top, right, bottom);
        return view;
    }

    private View obtainView(int row, int width, int height) {
        int itemType = mAdapter.getItemViewType(row);
        View recyclerView = recycler.get(itemType);
        View view = null;
        if (recyclerView == null) {
            view = mAdapter.onCreateViewHolder(row, recyclerView, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHolder 必须填充布局");
            }
        } else {
            view = mAdapter.onBinderViewHolder(row, recyclerView, this);
        }
        view.setTag(R.id.tag_type_view, itemType);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }


    private int sumArray(int[] array, int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        if (adapter != null) {
            recycler = new Recycler(adapter.getViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            //重新绘制
            requestLayout();
        }
    }

    /**
     * 适配器
     */
    interface Adapter {
        /**
         * item的类型
         *
         * @param row
         * @return
         */
        int getItemViewType(int row);

        /**
         * 获取item类型的个数
         *
         * @return
         */
        int getViewTypeCount();

        /**
         * 创建ViewHolder
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        View onCreateViewHolder(int position, View convertView, ViewGroup parent);

        /**
         * 绑定viewHolder 加载数据
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        View onBinderViewHolder(int position, View convertView, ViewGroup parent);

        /**
         * 获取当前高度
         *
         * @param index
         * @return
         */
        int getHeight(int index);

        /**
         * 获取当前item数量
         *
         * @return
         */
        int getCount();
    }

}
