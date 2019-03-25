package com.lee.code.uidraw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lee.code.uidraw.R;

/**
 * 刮刮卡 view
 */
public class GGK extends View {
    private Paint mPaint;
    private Bitmap mDstBmp,mSrcBmp;
    private Path mPath;
    private String text1 = "恭喜您，获得了一等奖！";
    private String text2 = "刮一刮";

    public GGK(Context context) {
        this(context, null);
    }

    public GGK(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GGK(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);

        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mSrcBmp = BitmapFactory.decodeResource(getResources(), R.drawable.shave_bg);
        mPath = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDstBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制底层中奖字体
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(36);

        Rect bounds = new Rect();
        mPaint.getTextBounds(text1, 0, text1.length(), bounds);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.top;
        canvas.drawText(text1, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);

        //使用离屏绘制
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setStrokeWidth(80);

        Canvas dstCanvas = new Canvas(mDstBmp);
        dstCanvas.drawPath(mPath,mPaint);

        canvas.drawBitmap(mDstBmp,0,0,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(mSrcBmp,new Rect(0,0,mSrcBmp.getWidth(),mSrcBmp.getHeight()),new Rect(0,0,getWidth(),getHeight()),mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    private float eventX,eventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventX = event.getX();
                eventY = event.getY();
                mPath.moveTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - eventX) / 2 + eventX;
                float endY = (event.getY() - eventY) / 2 + eventY;
                mPath.quadTo(eventX, eventY, endX, endY);
                eventY = event.getY();
                eventX = event.getX();
                break;
        }
        invalidate();
        return true;
    }

}
