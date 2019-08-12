package com.lee.library.widget.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.lee.library.R;

/**
 * @author jv.lee
 * @date 2019/5/7
 */
public class DotNumberView extends View {
    private Paint mBackgroundPaint;
    private Paint mNumberPaint;
    private int mWidth;
    private int mHeight;
    private int backgroundColor = Color.RED;
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
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStrokeWidth(1);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
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
        String count = String.valueOf(numberCount);

        Rect bounds = new Rect();
        mNumberPaint.setTextSize((float) (mHeight * 0.6));
        mNumberPaint.getTextBounds(count, 0, count.length(), bounds);
        float offSet = (bounds.top + bounds.bottom) / 2;

        canvas.drawText(count, mWidth / 2, (mHeight / 2) - offSet, mNumberPaint);
    }

    private void drawBackground(Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.dot_shape);
        if (drawable != null) {
            setBackground(drawable);
        } else {
            mBackgroundPaint.setColor(backgroundColor);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mBackgroundPaint);
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setNumberColor(int numberColor) {
        this.numberColor = numberColor;
    }

    public void setNumberCount(int numberCount) {
        this.numberCount = numberCount;
        invalidate();
    }
}
