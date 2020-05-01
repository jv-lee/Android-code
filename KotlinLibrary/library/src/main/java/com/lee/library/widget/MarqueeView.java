package com.lee.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lee.library.R;
import com.lee.library.utils.SizeUtil;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/5/24.
 * description：跑马灯
 */
public class MarqueeView extends FrameLayout {

    private List<String> data;
    private int currentIndex = 0;

    private TextView tvContent;
    private Drawable leftDrawable;
    private int fontColor;
    private float fontSize;

    private int width;
    private int height;
    private AnimatorSet set;
    private boolean hasStop = false;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttrs(context, attrs);
        initView();
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView);
        fontColor = typedArray.getColor(R.styleable.MarqueeView_font_color, Color.WHITE);
        fontSize = typedArray.getDimension(R.styleable.MarqueeView_font_size, SizeUtil.px2sp(getContext(), 16));
        leftDrawable = typedArray.getDrawable(R.styleable.MarqueeView_left_drawable);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.makeMeasureSpec(widthMeasureSpec,
                MeasureSpec.UNSPECIFIED);
        height = MeasureSpec.makeMeasureSpec(heightMeasureSpec,
                MeasureSpec.UNSPECIFIED);
        initAnim();
    }

    private void initView() {
        tvContent = new TextView(getContext());
        tvContent.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvContent.setGravity(Gravity.CENTER_VERTICAL);
        tvContent.setTextSize(SizeUtil.px2sp(getContext(), fontSize));
        tvContent.setTextColor(fontColor);
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            tvContent.setCompoundDrawables(leftDrawable, null, null, null);
        }
        addView(tvContent);
    }

    private void initAnim() {
        ObjectAnimator in = ObjectAnimator.ofFloat(tvContent, View.TRANSLATION_Y, height, 0f);
        ObjectAnimator out = ObjectAnimator.ofFloat(tvContent, View.TRANSLATION_Y, 0f, -height);
        out.setStartDelay(500);
        set = new AnimatorSet();
        set.play(in).before(out);
        set.setDuration(1000);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvContent.setText(data.get(currentIndex++));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!hasStop) {
                    if (currentIndex < data.size()) {
                        set.start();
                    } else {
                        currentIndex = 0;
                        set.start();
                    }
                }
            }
        });
    }

    public void bindData(List<String> data) {
        if (set != null) {
            currentIndex = 0;
            pause();
        }
        this.data = data;
    }

    public void start() {
        if (data == null || data.size() == 0) {
            return;
        }
        hasStop = false;
        set.start();
    }

    public void pause() {
        hasStop = true;
        set.end();
    }

}

