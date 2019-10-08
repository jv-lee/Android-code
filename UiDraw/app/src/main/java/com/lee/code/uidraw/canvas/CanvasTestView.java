package com.lee.code.uidraw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jv.lee
 * @date 2019/10/8.
 * @description
 */
public class CanvasTestView extends View {

    private Paint mPaint;

    public CanvasTestView(Context context) {
        this(context, null);
    }

    public CanvasTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(300, 300, 600, 600, mPaint);
        int saveLayer = canvas.saveLayer(0, 0, 700, 700, mPaint);
        canvas.translate(100, 100);
        canvas.drawRect(0, 0, 700, 700, mPaint);
//        canvas.restore();
        canvas.restoreToCount(saveLayer);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, 100, 100, mPaint);
    }
}
