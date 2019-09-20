package com.lee.app.view.header;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import com.lee.app.R;
import com.lee.library.widget.ItemNotificationView;
import com.lee.library.widget.refresh.header.BaseHeaderView;

/**
 * 自定义刷新头
 * @author jv.lee
 */
public class SimpleHeader extends BaseHeaderView {
    private static final String TAG = "SimpleHeader";
    /**
     * 刷新头的状态
     */
    private int status;
    /**
     * 头布局高度
     */
    private int childHeight;
    /**
     * 布局偏移量
     */
    private float totalOffset;
    /**
     * 更新提示数据
     */
    private String updateText;

    /**
     * 刷新头布局视图内容
     */
    private ProgressBar progress;
    private ItemNotificationView notificationView;

    private ObjectAnimator progressAnimator;

    public SimpleHeader(Context context) {
        this(context, null);
    }

    public SimpleHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        status = HEADER_DRAG;
        childHeight = context.getResources().getDimensionPixelSize(R.dimen.default_height);

        //添加头内容
        View view = LayoutInflater.from(context).inflate(R.layout.header_simple, this, false);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childHeight);
        addView(view, lp);

        progress = view.findViewById(R.id.progress);
        notificationView = view.findViewById(R.id.item_notification);
        notificationView.setHideEnable(false);

        //添加头部动画
        progressAnimator = ObjectAnimator.ofFloat(progress, View.ROTATION, 0, 359);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.setDuration(1000);
        progressAnimator.setRepeatCount(Animation.INFINITE);
    }

    /**
     * 将移动的距离传递过来
     */
    @Override
    public void handleDrag(float dragY) {
        this.totalOffset = dragY;
        if (canTranslation) {
            setTranslationY(dragY);
        }
        //只要是正在刷新
        if (status == HEADER_REFRESHING) {
            return;
        }
        //TODO 回到初始位置 下拉刷新
        if (dragY <= 0) {
            status = HEADER_DRAG;
            enableProgress(true);
        }
        //开始拖动
        if (status == HEADER_DRAG) {
            progress.setRotation(dragY);
            //TODO 一旦超过刷新头高度  释放更新
            if (dragY >= childHeight) {
                status = HEADER_RELEASE;
            }
        }
        //还未释放拖拉回去
        if (status == HEADER_RELEASE) {
            progress.setRotation(dragY);
            //TODO  一旦低于刷新头高度 下拉刷新
            if (dragY <= childHeight) {
                status = HEADER_DRAG;
            }
        }
    }

    @Override
    public boolean doRefresh() {
        //TODO 正在刷新，并且偏移量==刷新头高度才认为刷新
        if (status == HEADER_REFRESHING && totalOffset == childHeight) {
            startProgress();
            return true;
        }
        return false;
    }

    @Override
    public void setParent(ViewGroup parent) {
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = -childHeight;
        parent.addView(this, lp);
    }

    @Override
    public boolean checkRefresh() {
        if ((status == HEADER_RELEASE || status == HEADER_REFRESHING) && totalOffset >= childHeight) {
            //TODO 加载中
            status = HEADER_REFRESHING;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refreshCompleted() {
        Log.i(TAG, "refreshCompleted: ");
        status = HEADER_COMPLETED;
        //TODO 刷新完毕
        stopProgress();
        enableProgress(false);
        notificationView.update(updateText);
    }

    @Override
    public int getHeaderHeight() {
        return childHeight;
    }

    @Override
    public void autoRefresh() {
        status = HEADER_REFRESHING;
        startProgress();
    }

    private void enableProgress(boolean enable) {
        if (enable) {
            progress.setVisibility(VISIBLE);
            notificationView.setVisibility(GONE);
        }else{
            progress.setVisibility(GONE);
            notificationView.setVisibility(VISIBLE);
        }
    }

    private void startProgress() {
        if (progressAnimator != null && !progressAnimator.isRunning()) {
            progressAnimator.start();
        }
    }

    private void stopProgress() {
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }
    }


    public String getUpdateText() {
        return updateText;
    }

    public void setUpdateText(String updateText) {
        this.updateText = updateText;
    }
}
