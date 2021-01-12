package com.lee.library.widget.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author jv.lee
 * @date 2019/5/7
 */
public class DotNumberView extends View {
    private Paint mNumberPaint;
    private Rect mTextBounds;
    private int mWidth;
    private int mHeight;
    private int backgroundColor = Color.parseColor("#FF665B");
    private int numberColor = Color.WHITE;
    private int numberCount = 0;

    public DotNumberView(Context context) {
        this(context, null);
    }

    public DotNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint.setColor(numberColor);
        mNumberPaint.setStrokeWidth(1);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (drawCount()) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));
            drawBackground(canvas);
            drawTextCount(canvas);
        }
    }

    private boolean drawCount() {
        if (numberCount <= 0) {
            setVisibility(GONE);
            return false;
        }
        setVisibility(VISIBLE);
        return true;
    }

    private void drawTextCount(Canvas canvas) {
        mNumberPaint.setColor(numberColor);
        String count = parseNumberStr(numberCount);

        Rect bounds = new Rect();
        mNumberPaint.setTextSize((float) (mHeight * 0.75));
        mNumberPaint.getTextBounds(count, 0, count.length(), bounds);
        float offSet = (bounds.top + bounds.bottom) / 2;

        canvas.drawText(count, mWidth / 2 - scaleSize(), (mHeight / 2) - offSet, mNumberPaint);
    }

    private void drawBackground(Canvas canvas) {
        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(backgroundColor);
        pressedDrawable.setCornerRadius(mHeight / 2);
        setBackground(pressedDrawable);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        postInvalidate();
    }

    public void setNumberColor(int numberColor) {
        this.numberColor = numberColor;
        postInvalidate();
    }

    public void setNumberCount(int numberCount) {
        this.numberCount = numberCount;
        buildLayoutSize();
        requestLayout();
    }

    private String parseNumberStr(int number) {
        if (number >= 999) return "999+";
        return String.valueOf(number);
    }

    private void buildLayoutSize() {
        String count = parseNumberStr(numberCount);
        mTextBounds = new Rect();
        mNumberPaint.setTextSize(10);
        mNumberPaint.getTextBounds(count, 0, count.length(), mTextBounds);

        ViewGroup.MarginLayoutParams layoutParams = buildLayoutParams();
        setLayoutParams(layoutParams);
    }

    private ViewGroup.MarginLayoutParams buildLayoutParams() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.width = buildWidth();
        layoutParams.height = buildHeight();
        return layoutParams;
    }

    private int buildWidth() {
        int width = 0;
        if (numberCount < 10) {
            width = dp2px(16);
        } else if (numberCount < 999) {
            width = dp2px(mTextBounds.width()) + dp2px(10);
        } else {
            width = dp2px(mTextBounds.width()) + dp2px(10);
        }
        return width;
    }

    private int buildHeight() {
        return dp2px(16);
    }

    private float scaleSize() {
        if (numberCount > 10 && numberCount < 999 && String.valueOf(numberCount).substring(0, 1).equals("1")) {
            return 1F;
        } else {
            return 0f;
        }
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
