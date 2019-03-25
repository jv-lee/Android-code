package com.lee.code.uidraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 状态保存和恢复
 */
public class SaveRestoreView extends View {
    private Paint mPaint;

    public SaveRestoreView(Context context) {
        this(context,null);
    }

    public SaveRestoreView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SaveRestoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        saveLayer(canvas);
    }

    private void saveOrRestore(Canvas canvas) {
        Log.i("lee >>> ", "omDraw:"+canvas.getSaveCount());
        canvas.drawRect(200,200,700,700,mPaint);
        int save = canvas.save();//保存画布状态
        Log.i("lee >>> ", "omDraw:"+canvas.getSaveCount());
        canvas.translate(50,50);

        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0,0,500,500,mPaint);

        canvas.save();
        Log.i("lee >>> ", "omDraw:"+canvas.getSaveCount());
        canvas.translate(50,50);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0,0,500,500,mPaint);

//        canvas.restore();//恢复画布状态 可以调用多次  回到上一次 再次调用回到上上一次
        Log.i("lee >>> ", "omDraw:"+canvas.getSaveCount());
//        canvas.restore();
        canvas.restoreToCount(save); //通过save值直接返回该状态 如果是第一个状态 后面的状态将直接销毁
        Log.i("lee >>> ", "omDraw:"+canvas.getSaveCount());
        if (canvas.getSaveCount() > 1) {
            canvas.restore();
        }
        canvas.drawLine(0,0,400,500,mPaint);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void saveLayer(Canvas canvas) {
        canvas.drawRect(200,200,700,700,mPaint);

        int layerId = canvas.saveLayer(0, 0, 700,700, mPaint);
        mPaint.setColor(Color.YELLOW);
        Matrix matrix = new Matrix();
        matrix.setTranslate(100,100);
        canvas.setMatrix(matrix);
        canvas.drawRect(0,0,700,700,mPaint);//由于平移操作 导致绘制的矩形超出了图层的大小，所以绘制不完全
        canvas.restoreToCount(layerId);

        mPaint.setColor(Color.RED);
        canvas.drawRect(0,0,100,100,mPaint);
    }
}
