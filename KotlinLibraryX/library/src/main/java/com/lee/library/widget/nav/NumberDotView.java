package com.lee.library.widget.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jv.lee
 * @date 2019/5/7
 */
public class NumberDotView extends View {
    private Paint mBackgroundPaint;
    private Paint mNumberPaint;
    private Rect mTextBounds;
    private int mWidth;
    private int mHeight;
    private int backgroundColor = Color.parseColor("#FF665B");
    private int numberColor = Color.WHITE;
    private int numberCount = 0;
    private final String MAX_VALUE = "999+";

    public NumberDotView(Context context) {
        this(context, null);
    }

    public NumberDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initTextBound();
    }

    private void initPaint() {
        mNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint.setColor(numberColor);
        mNumberPaint.setStrokeWidth(1);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);
        mNumberPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextBound() {
        String text = parseNumberStr(numberCount);
        mTextBounds = new Rect();
        mNumberPaint.setTextSize(10);
        mNumberPaint.getTextBounds(text, 0, text.length(), mTextBounds);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(layoutWidth(), layoutHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (drawCount()) {
            drawBackground(canvas);
            drawTextCount(canvas);
        }
    }

    private boolean drawCount() {
        return numberCount > 0;
    }

    private void drawTextCount(Canvas canvas) {
        mNumberPaint.setColor(numberColor);
        String count = parseNumberStr(numberCount);

        Rect bounds = new Rect();
        mNumberPaint.setTextSize((float) (mHeight * 0.75));
        mNumberPaint.getTextBounds(count, 0, count.length(), bounds);
        float offSet = (bounds.top + bounds.bottom) / 2;

        canvas.drawText(count, mWidth / 2, (mHeight / 2) - offSet, mNumberPaint);
    }

    private void drawBackground(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = mWidth;
        rectF.top = 0;
        rectF.bottom = mHeight;
        canvas.drawRoundRect(rectF, mHeight / 2, mHeight / 2, mBackgroundPaint);
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
        requestLayout();
        initTextBound();
        postInvalidate();
    }

    private String parseNumberStr(int number) {
        if (number >= 999) return MAX_VALUE;
        return String.valueOf(number);
    }


    private int layoutWidth() {
        int width = 0;
        if (numberCount < 10) {
            width = dp2px(16);
        } else {
            width = dp2px(mTextBounds.width()) + dp2px(10);
        }
        return width;
    }

    private int layoutHeight() {
        return dp2px(16);
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
