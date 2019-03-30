package com.lee.library.refresh;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.lee.library.refresh.footer.BaseFooterView;
import com.lee.library.refresh.footer.DefaultFooter;
import com.lee.library.refresh.header.BaseHeaderView;
import com.lee.library.refresh.header.DefaultHeader;

/**
 * 继承于帧布局，下拉刷新、上拉加载更多的容器；
 * 功能：
 * 1、使布局上拉下拉具有回弹效果；
 * 2、可以只有上拉刷新；
 * 3、能够auto刷新（界面进入就刷新）；
 * 4、能够快速自定义刷新头，刷新效果；
 * 5、刷新开始、刷新结束回调；
 * 6、加载更多开始、结束回调；
 * 7、必须要有实际宽高才能激活下拉刷新和加载更多；
 * 8、能够结合RecyclerView、ListView、ScrollerView、WebView等相关变化的View，能够同时结合列表+ViewPager+侧拉删除的结构，能够结合侧拉；
 * 9、能够管理多指触控；
 * 10、强烈的阻尼效果
 * 11、子view不能阻止其回弹效果
 *
 * @author tangxianqiang
 */

public class RefreshLayout extends FrameLayout {

    private static final String TAG = "BounceLayout";
    /*滚动实例*/
    private Scroller mScroller;
    /*设备滚动间隙最小值*/
    private int mTouchSlop;
    /*手指按下的y位置*/
    private float mYDown;
    /*手机上一次移动的y轴位置,也是在拦截事件前最后的y*/
    private float mYLastMove;
    /*上次移动的x值*/
    private float mXDown;
    /*手指在不断移动时的y轴的实时坐标，不管任何手指,它总是该布局移动的决定值*/
    private float mYMove;
    /*多点触控的时候，记录总的偏移量*/
    private float totalOffsetY;
    /*移动进行时，此时对应的手指*/
    private int currentPointer;
    /*移动进行时，处理多指对应的y*/
    private float currentY;
    /*手指index变化*/
    private boolean pointerChange;
    /*保证随时按下都可以开始滑动*/
    private boolean forceDrag;
    /*布局的高度*/
    private float height;
    /*阻尼系数*/
    private float dampingCoefficient = 3.5f;
    /*控制内容上拉和下拉的处理者，可以自己定义*/
    private RefreshHandler bounceHandler;
    /*滚动的孩子*/
    private View childContent;
    /*头布局*/
    private BaseHeaderView headerView;
    /*底部布局*/
    private BaseFooterView footerView;
    /*孩子一旦得到事件后，不还给父亲*/
    private boolean alwaysDispatch;
    /*是否暂停回弹*/
//    private boolean bounceStopped;
    /*刷新锁,防止同时回调多个刷新操作*/
    private boolean lockBoolean;
    /*是否固定回弹布局*/
    private boolean disallowBounce;
    private boolean dispathAble = true;


    public RefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //初始化滚动实例,使用默认的变速插值器
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    /**
     * 使用事件分发方法来处理逻辑，防止拦截了孩子的事件后，导致子view本次touch事件永远得不到事件，所以不再onInterceptTouchEvent中做处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //三者为空，直接认为不拦截，导致有冲突地方将不会出现刷新头
        if (forwardingHelper == null || bounceHandler == null
                || childContent == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                alwaysDispatch = false;
                currentPointer = 0;
                // getRawY表示相对屏幕的位置，也就是绝对位置 和 getY表示相对父亲的位置
                mYDown = ev.getY();
                mXDown = ev.getX();
                //这样做是为了初始化 mYLastMove
                mYLastMove = ev.getY();
                currentY = mYDown;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                currentPointer = ev.getActionIndex();
                currentY = ev.getY(currentPointer);
                pointerChange = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerChange = true;
                //说明即将只有一根手指了，事件以第一根手指为准
                if (ev.getPointerCount() == 2) {
                    currentPointer = 0;
                    currentY = mYLastMove;
                } else {
                    //离开的是最近的手指，事件以第一根手指为准
                    if (currentPointer == ev.getActionIndex()) {
                        currentPointer = 0;
                        currentY = mYLastMove;
                        //事件以最后一根手指为准
                    } else {
                        currentPointer = ev.getPointerCount() - 1 - 1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = ev.getY(currentPointer);
                //手指变化后先转化之前的值，不然会导致抖动
                if (pointerChange) {
                    currentY = mYMove;
                }
                //notForwarding做的是第一步骤的拦截判断
                if ((forwardingHelper.notForwarding(mXDown, mYDown, ev.getX(), ev.getY()) && !alwaysDispatch) || forceDrag) {
                    //转发给孩子
                    if (dispatchToChild(ev.getY(currentPointer))) {
                        currentY = mYMove;
                        return super.dispatchTouchEvent(ev);
                    } else {
                        moving(ev);
                        currentY = mYMove;
                        //触发移动事件 拦截事件传递
                        return super.dispatchTouchEvent(ev);
                    }
                    //父亲转发事件到孩子
                } else {
                    alwaysDispatch = dispathAble;
                    currentY = mYMove;
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                scroll = false;
            case MotionEvent.ACTION_CANCEL:
                forceDrag = false;
                alwaysDispatch = false;
                //再抬起手指时都需要判断是否显示footer、header(之前footer、header可能被拉出来需要刷新或者加载更多)
                if (headerView != null && headerView.checkRefresh()) {
                    if (disallowBounce) {
                        if (!lockBoolean) {
                            refreshCallBack.onRefresh();
                            lockBoolean = true;
                        }
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + headerView.getHeaderHeight()), 500);
                        invalidate();
                    }
                    break;
                }
                if (footerView != null && footerView.checkLoading()) {
                    if (disallowBounce) {
                        if (!lockBoolean) {
                            loadingMoreCallBack.onLoadingMore();
                            lockBoolean = true;
                        }
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - footerView.getFooterHeight()), 500);
                        invalidate();
                    }
                    break;
                }
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 500);
                invalidate();
                break;
            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean scroll = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_CANCEL:
                //当前是处于拉出状态
                if (totalOffsetY != 0) {
                    return true;
                } else {
                    return super.onInterceptTouchEvent(ev);
                }
            default:
        }
        return super.onInterceptTouchEvent(ev);

    }


    /**
     * 竖直方向判断是否需要将事件转发给孩子
     *
     * @return
     */
    private boolean dispatchToChild(float movingY) {
        boolean moveDown = currentY < movingY;
        //只要处于拉升状态，都不转发
        if (getScrollY() != 0) {
            return false;
        }
        if (moveDown && bounceHandler.canChildDrag(childContent)) {
            return false;
        }
        if (!moveDown && bounceHandler.canChildPull(childContent)) {
            return false;
        }
        //换手
        if (currentY == movingY) {
            return false;
        }
        //bounceLayout固定不动，并且header被拉出来
        if (disallowBounce && totalOffsetY != 0) {
            return false;
        }
        //默认都是要转发给孩子的
        return true;
    }

    /**
     * 布局真正开始移动，而非view的滚动
     *
     * @param ev
     */
    private void moving(MotionEvent ev) {
        forceDrag = true;
        float scrollY = mYMove - currentY;
        float p = Math.abs(totalOffsetY / height);
        //保证永远不能拉到布局不可见的状态
        if (p == 1) {
            p = 1 - Integer.MIN_VALUE;
        }
        scrollY = scrollY / (dampingCoefficient * (1.0f / (1 - p)));
        float offsetY = totalOffsetY + scrollY;
        //存在临界点变相
        if (offsetY * totalOffsetY < 0) {
            totalOffsetY = 0;
        } else {
            totalOffsetY = offsetY;
        }
        //不允许拉动
        if (!disallowBounce) {
            scrollTo(0, (int) -totalOffsetY);
        }
        pointerChange = false;
        //在布局拉动的时候一定要将拉动的值传到header
        if (headerView != null) {
            headerView.handleDrag(totalOffsetY);
        }
        //在布局拉动的时候一定要将拉动的值传到footer
        if (footerView != null) {
            footerView.handlePull(totalOffsetY);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setClickable(true);
        for (int i = 0; i < getChildCount(); i++) {
            if (!getChildAt(i).isClickable()) {
                getChildAt(i).setClickable(true);
            }
        }
        height = h;
    }

    /**
     * 加载完成的回弹不能阻止
     */
    @Override
    public void computeScroll() {
        if (forceDrag) {
            return;
        }
        //需要停止回弹，开始加载。。。
        if (mScroller.computeScrollOffset()) {
            totalOffsetY = -mScroller.getCurrY();
            scrollTo(0, mScroller.getCurrY());
            invalidate();
            if (headerView != null) {
                headerView.handleDrag(totalOffsetY);
                //刷新头开始正式刷新
                if (headerView.doRefresh()) {
                    if (!lockBoolean) {
                        if (refreshCallBack != null) {
                            refreshCallBack.onRefresh();
                            forceDrag = true;
                            lockBoolean = true;
                        }
                    }
                }
            }
            if (footerView != null) {
                footerView.handlePull(totalOffsetY);
                ////底部加载更多
                if (footerView.doLoading()) {
                    if (!lockBoolean) {
                        if (refreshCallBack != null) {
                            loadingMoreCallBack.onLoadingMore();
                            forceDrag = true;
                            lockBoolean = true;
                        }
                    }
                }
            }

        }
    }

    public void setBounceHandler(RefreshHandler bounceHandler, View v) {
        this.bounceHandler = bounceHandler;
        this.childContent = v;
    }

    public void setEventForwardingHelper(EventForwardingHelper forwardingHelper) {
        this.forwardingHelper = forwardingHelper;
    }

    private EventForwardingHelper forwardingHelper;

    private ViewGroup parent;

    public void setDefaultView(ViewGroup parent, View view) {
        this.parent = parent;
        setHeaderView(new DefaultHeader(getContext()));
        setFooterView(new DefaultFooter(getContext()));
        setHandlerHelper(view);
    }

    public void setBootView(ViewGroup parent, View view, BaseHeaderView headerView, BaseFooterView footerView) {
        this.parent = parent;
        if (headerView != null) {
            setHeaderView(headerView);
        }
        if (footerView != null) {
            setFooterView(footerView);
        }
        setHandlerHelper(view);
    }

    public void setHandlerHelper(View view) {
        this.setBounceHandler(new NormalRefreshHandler(), view);
        this.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
    }

    public void setHeaderView(BaseHeaderView headerView) {
        this.headerView = headerView;
        if (headerView != null) {
            headerView.setParent(parent);
            if (disallowBounce) {
                headerView.setCanTranslation(false);
            }
        }
    }

    public void setFooterView(BaseFooterView footerView) {
        this.footerView = footerView;
        if (footerView != null) {
            footerView.setParent(parent);
            if (disallowBounce) {
                footerView.setCanTranslation(false);
            }
        }
    }

    private RefreshCallBack refreshCallBack;
    private LoadingMoreCallBack loadingMoreCallBack;

    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }

    public void setLoadingMoreCallBack(LoadingMoreCallBack loadingMoreCallBackCallBack) {
        this.loadingMoreCallBack = loadingMoreCallBackCallBack;
    }

    /**
     * 完成加载
     */
    public void setRefreshCompleted() {
        headerView.refreshCompleted();
        lockBoolean = false;
        forceDrag = false;
        if (disallowBounce) {
            totalOffsetY = 0;
            headerView.handleDrag(0);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                    invalidate();
                }
            }, 800);
        }
        ;
    }

    /**
     * 完成加载更多
     */
    public void setLoadingMoreCompleted() {
        footerView.LoadingCompleted();
        lockBoolean = false;
        forceDrag = false;
        if (disallowBounce) {
            totalOffsetY = 0;
            footerView.handlePull(0);
        } else {
            //一个定时器，消息池管理等待事件
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                    invalidate();
                }
            }, 800);
        }

    }

    public void setDampingCoefficient(float dampingCoefficient) {
        this.dampingCoefficient = dampingCoefficient;
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (headerView == null) {
            return;
        }
        headerView.autoRefresh();
        //固定回弹布局
        if (!disallowBounce) {
            mScroller.startScroll(0, 0, 0, -headerView.getHeaderHeight(), 0);
            invalidate();
        } else {
            if (!lockBoolean) {
                refreshCallBack.onRefresh();
                forceDrag = true;
                lockBoolean = true;
            }
        }
    }

    public void setDisallowBounce(boolean disallowBounce) {
        this.disallowBounce = disallowBounce;
    }

    public void serDispatchAble(boolean able) {
        this.dispathAble = able;
    }
}
