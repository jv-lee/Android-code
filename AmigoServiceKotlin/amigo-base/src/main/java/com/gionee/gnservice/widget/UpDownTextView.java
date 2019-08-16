package com.gionee.gnservice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class UpDownTextView extends LinearLayout {
    private Context mContext;
    private TextView textViews[] = new TextView[3];
    private LinearLayout llayout;
    private String curText = null;
    private int mAnimTime = 500;
    private int mStillTime = 500;
    private List<String> mTextList;
    private int currentIndex = 0;
    private int animMode = ANIM_MODE_UP;

    public final static int ANIM_MODE_UP = 0;
    public final static int ANIM_MODE_DOWN = 1;

    private TranslateAnimation animationDown, animationUp;

    public UpDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews();
    }

    private void initViews() {
        llayout = new LinearLayout(mContext);
        llayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(llayout);

        textViews[0] = addText();
        textViews[1] = addText();
        textViews[2] = addText();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setViewsHeight();
    }

    private void setViewsHeight() {
        for (TextView tv : textViews) {
            LayoutParams lp = (LayoutParams) tv.getLayoutParams();
            lp.height = getHeight();
            lp.width = getWidth();
            tv.setLayoutParams(lp);
        }

        LayoutParams lp2 = (LayoutParams) llayout.getLayoutParams();
        lp2.height = getHeight() * (llayout.getChildCount());
        lp2.setMargins(0, -getHeight(), 0, 0);
        llayout.setLayoutParams(lp2);
    }

    public void setGravity(int graty) {
        for (TextView tv : textViews) {
            tv.setGravity(graty);
        }
    }

    public void setTextSize(int dpSize) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpSize);
        }
    }

    public void setTextColor(int color) {
        for (TextView tv : textViews) {
            tv.setTextColor(color);
        }
    }

    private TextView addText() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        llayout.addView(tv);
        return tv;
    }

    public void setText(String curText) {
        this.curText = curText;
        textViews[1].setText(curText);
    }

    public void startAutoScroll() {
        if (mTextList == null || mTextList.size() == 0) {
            return;
        }
        stopAutoScroll();
        this.postDelayed(runnable, mStillTime);
    }

    public void stopAutoScroll() {
        this.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentIndex = (currentIndex) % mTextList.size();
            switch (animMode) {
                case ANIM_MODE_UP:
                    setTextUpAnim(mTextList.get(currentIndex));
                    break;
                case ANIM_MODE_DOWN:
                    setTextDownAnim(mTextList.get(currentIndex));
                    break;
                default:
                    break;
            }
            currentIndex++;
            UpDownTextView.this.postDelayed(runnable, mStillTime + mAnimTime);

        }
    };

    public void setTextUpAnim(String text) {
        this.curText = text;
        textViews[2].setText(text);
        up();
    }

    public void setTextDownAnim(String text) {
        this.curText = text;
        textViews[0].setText(text);
        down();
    }

    public void setDuring(int during) {
        this.mAnimTime = during;
    }

    private void up() {
        llayout.clearAnimation();
        if (animationUp == null)
            animationUp = new TranslateAnimation(0, 0, 0, -getHeight());
        animationUp.setDuration(mAnimTime);
        llayout.startAnimation(animationUp);
        animationUp.setAnimationListener(listener);
    }

    public void down() {
        llayout.clearAnimation();
        if (animationDown == null)
            animationDown = new TranslateAnimation(0, 0, 0, getHeight());
        animationDown.setDuration(mAnimTime);
        llayout.startAnimation(animationDown);
        animationDown.setAnimationListener(listener);
    }

    private AnimationListener listener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation arg0) {
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            setText(curText);
        }
    };

    public int getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(int mAnimTime) {
        this.mAnimTime = mAnimTime;
    }

    public int getStillTime() {
        return mStillTime;
    }

    public void setStillTime(int mStillTime) {
        this.mStillTime = mStillTime;
    }

    public List<String> getTextList() {
        return mTextList;
    }

    public void setTextList(List<String> mTextList) {
        this.mTextList = mTextList;
    }

    public void setTextList(String[] mTextList) {
        this.mTextList = Arrays.asList(mTextList);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getAnimMode() {
        return animMode;
    }

    public void setAnimMode(int animMode) {
        this.animMode = animMode;
    }

}
