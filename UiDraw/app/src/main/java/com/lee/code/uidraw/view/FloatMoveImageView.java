package com.lee.code.uidraw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author jv.lee
 * @date 2019/5/1
 * @description 可拖拽浮动ImageView
 */
@SuppressLint("AppCompatCustomView")
public class FloatMoveImageView extends ImageView {

    private final String TAG = FloatMoveImageView.class.getSimpleName();

    private int overMode = OVER_MODE_PULLOVER;

    /**
     * 任意拖动重置初始化位置
     */
    public static final int OVER_MODE_REINDEX = 0x0001;

    /**
     * 任意位置回弹到边界
     */
    public static final int OVER_MODE_PULLOVER = 0X0002;

    /**
     * 边界回弹，中心支持悬停
     */
    public static final int OVER_MODE_HOVER = 0X0003;

    private int mOverWidth;
    private int mOverHeight;
    private int mParentWidthClip;
    private int mEndX;
    private int mEndY;

    public FloatMoveImageView(Context context) {
        super(context);
    }

    public FloatMoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatMoveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup mViewGroup = (ViewGroup) getParent();
        if (mViewGroup != null) {
            int mParentWidth = mViewGroup.getWidth();
            int mParentHeight = mViewGroup.getHeight();
            mParentWidthClip = mParentWidth / 2;
            mOverWidth = mParentWidth - getWidth();
            mOverHeight = mParentHeight - getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "TouchMoveDown");
                break;
            case MotionEvent.ACTION_MOVE:
                setClickable(false);
                //计算距离上次移动了多远
                int currX = x - mEndX;
                int currY = y - mEndY;
                //设置当前偏移量实现拖动
                this.setTranslationX(this.getTranslationX() + currX);
                this.setTranslationY(this.getTranslationY() + currY);
                break;
            case MotionEvent.ACTION_UP:
                setClickable(true);
                switchMode();
                break;
            default:
                break;
        }
        mEndX = x;
        mEndY = y;
        return true;
    }

    /**
     * 位置重置
     */
    private void onReIndex() {
        //平移回到该view水平方向的初始点
        this.setTranslationX(0);
        //判断什么情况下需要回到原点
        if (this.getY() < 0 || this.getY() > (this.getMeasuredHeight() - this.getMeasuredHeight())) {
            this.setTranslationY(0);
        }
    }

    /**
     * 越界回弹
     *
     * @param hover 是否支持悬停
     */
    private void onPullOver(boolean hover) {
        if (this.getX() < 0) {
            this.setTranslationX(0);
        } else if (this.getX() > mOverWidth) {
            this.setTranslationX(mOverWidth);
        } else if (!hover && this.getX() < mParentWidthClip) {
            this.setTranslationX(0);
        } else if (!hover && this.getX() > mParentWidthClip) {
            this.setTranslationX(mOverWidth);
        }

        if (this.getY() < 0) {
            this.setTranslationY(0);
        } else if (this.getY() > mOverHeight) {
            this.setTranslationY(mOverHeight);
        }
    }

    private void switchMode() {
        switch (overMode) {
            case OVER_MODE_REINDEX:
                onReIndex();
                break;
            case OVER_MODE_PULLOVER:
                onPullOver(false);
                break;
            case OVER_MODE_HOVER:
                onPullOver(true);
                break;
            default:
        }
    }

    /**
     * 设置回弹状态
     *
     * @param overMode OVER_MODE_REINDEX / OVER_MODE_PULLOVER / OVER_MODE_HOVER
     */
    public void setOverMode(int overMode) {
        this.overMode = overMode;
    }
}
