package com.lee.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import com.lee.library.utils.SizeUtil;

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
        notificationTextSize = typedArray.getDimension(R.styleable.ItemNotificationView_notification_text_size, SizeUtil.px2sp(getContext(), 20));
        backgroundColor = typedArray.getColor(R.styleable.ItemNotificationView_notification_color, Color.BLUE);
        typedArray.recycle();
    }

    private void init() {
        setVisibility(GONE);
        tvBackground = new TextView(getContext());
        tvBackground.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvBackground.setBackgroundColor(backgroundColor);

        tvContent = new TextView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        tvContent.setLayoutParams(layoutParams);
        tvContent.setTextSize(notificationTextSize);
        tvContent.setTextColor(notificationTextColor);
        tvContent.setText(notificationText);

        addView(tvBackground);
        addView(tvContent);
    }

    public void update(String text) {
        notificationText = text;
        setVisibility(VISIBLE);
        ObjectAnimator.ofFloat(tvBackground, ViewGroup.SCALE_X, 0.5F, 1F).setDuration(500).start();

        ObjectAnimator animator = ObjectAnimator.ofFloat(this, ViewGroup.TRANSLATION_Y, 0, -100).setDuration(500);
        animator.setStartDelay(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }
        });
        animator.start();

    }

}
