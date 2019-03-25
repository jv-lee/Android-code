package com.lee.code.uidraw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class TransformView extends View {

    private Paint mPaint;

    public TransformView(Context context) {
        this(context,null);
    }

    public TransformView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TransformView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        scale(canvas);
//        rotate(canvas);
//        skew(canvas);
//        clip(canvas);
//        clipOut(canvas);
        matrix(canvas);
    }

    /**
     * 1.平移操作
     * @param canvas
     */
    private void translate(Canvas canvas) {
        canvas.drawRect(0,0,400,400,mPaint);
        canvas.translate(50,50);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, 400, 400, mPaint);
        canvas.drawLine(0,0,600,600,mPaint);
    }

    /**
     * 2.缩放操作
     * @param canvas
     */
    private void scale(Canvas canvas) {
        canvas.drawRect(200, 200, 700, 700, mPaint);
        canvas.scale(0.5f,0.5f);
//        canvas.scale(0.5f,0.5f,200,200);// 重载方法 将画布起点平移到 200,200的位置 然后进行缩放 然后平移到-200，-200 其实就是将其实坐标向后平移
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(200,200,700,700,mPaint);
    }

    /**
     * 3.旋转操作
     * @param canvas
     */
    private void rotate(Canvas canvas) {
        canvas.drawRect(0,0,700,700,mPaint);
//        canvas.rotate(45);
        canvas.rotate(45,650,650);//重载方法  px py 表示旋转中心的坐标
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0,0,700,700,mPaint);
    }

    /**
     * 4.倾斜操作
     * @param canvas
     */
    private void skew(Canvas canvas) {
        canvas.drawRect(0,0,400,400,mPaint);
//        canvas.skew(1, 0);//在x方向上倾斜45度
        canvas.skew(0, 1);//在y方向倾斜45度
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0,0,400,400,mPaint);
    }

    /**
     * 5.切割操作
     * @param canvas
     */
    private void clip(Canvas canvas) {
        canvas.drawRect(200,200,700,700,mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(200,800,700,1300,mPaint);
        canvas.clipRect(200, 200, 700, 700); //画布被裁剪
        canvas.drawCircle(100,100,100,mPaint);  //超出裁剪区域，无法绘制
        canvas.drawCircle(300,300,100,mPaint); //坐标区域在裁剪区域内，绘制成功
    }

    /**
     * 6.反向切割操作
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clipOut(Canvas canvas) {
        canvas.drawRect(200,200,700,700,mPaint);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(200,800,700,1300,mPaint);
        canvas.clipOutRect(200, 200, 700, 700); //画布裁剪外的区域
        canvas.drawCircle(100,100,100,mPaint); //坐标区域在裁剪范围内，绘制成功
        canvas.drawCircle(300,300,100,mPaint);//坐标区域在裁剪范围外，绘制失败
    }

    /**
     * 矩阵操作
     * @param canvas
     */
    private void matrix(Canvas canvas) {
        canvas.drawRect(0,0,700,700,mPaint);
        Matrix matrix = new Matrix();
        matrix.setTranslate(50, 50);
        matrix.setRotate(45);
        matrix.setScale(0.5f,0.5f);
        canvas.setMatrix(matrix);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0,0,700,700,mPaint);
    }

}
