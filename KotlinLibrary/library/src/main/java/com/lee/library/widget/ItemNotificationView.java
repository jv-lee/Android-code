package com.lee.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.library.R;
import com.lee.library.utils.LogUtil;
import com.lee.library.utils.SizeUtil;

import static android.animation.ValueAnimator.INFINITE;

/**
 * @author jv.lee
 * @date 2019/7/8.
 * @description item更新条目提示view
 */
public class ItemNotificationView extends FrameLayout {

    private TextView tvBackground;
    private TextView tvContent;
    private int mWidth;
    private int mHeight;

    private int backgroundColor;
    private String notificationText;
    private int notificationTextColor;
    private float notificationTextSize;

    private boolean hideEnable = true;

    public ItemNotificationView(Context context) {
        this(context, null);
    }

    public ItemNotificationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemNotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(attrs);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    private void readAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemNotificationView);
        notificationText = typedArray.getString(R.styleable.ItemNotificationView_notification_text);
        notificationTextColor = typedArray.getColor(R.styleable.ItemNotificationView_notification_text_color, Color.WHITE);
        notificationTextSize = typedArray.getDimension(R.styleable.ItemNotificationView_notification_text_size, 16);
        backgroundColor = typedArray.getColor(R.styleable.ItemNotificationView_notification_color, Color.BLUE);
        typedArray.recycle();
    }

    private void init() {
        setVisibility(GONE);
        tvBackground = new TextView(getContext());
        tvBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvBackground.setBackgroundColor(backgroundColor);

        tvContent = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        tvContent.setLayoutParams(layoutParams);
        tvContent.setTextSize(notificationTextSize);
        tvContent.setTextColor(notificationTextColor);
        tvContent.setText(notificationText);

        addView(tvBackground);
        addView(tvContent);
    }

    private void anim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, ViewGroup.TRANSLATION_Y, 0, -100).setDuration(500);
        animator.setStartDelay(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setY(0);
                setVisibility(GONE);
            }
        });
        animator.start();
    }

    @SuppressLint("WrongConstant")
    public void update(String text) {
        post(() -> {
            tvContent.setText(text);

            setVisibility(VISIBLE);
            ObjectAnimator objAnim = ObjectAnimator.ofFloat(tvBackground, ViewGroup.SCALE_X, 0.3F, 1F).setDuration(500);
            objAnim.setRepeatMode(ValueAnimator.INFINITE);
            objAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (hideEnable) {
                        anim();
                    }
                }
            });
            objAnim.start();
        });
    }

    public void setHideEnable(boolean hideEnable) {
        this.hideEnable = hideEnable;
    }
}
