package com.lee.code.uidraw.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    private Paint mPaint;
    private Path mPath;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(Color.RED);

        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        add(canvas);
//        to(canvas);
//        to2(canvas);
//        quadTo(canvas);
        cubicTo(canvas);
    }

    /**
     * 三阶贝塞尔曲线
     * @param canvas
     */
    private void cubicTo(Canvas canvas) {
        //起点0,100  第一个数据点100,200
        mPath.moveTo(0,100);
        mPath.cubicTo(100,200,200,0,300,200);
        canvas.drawPath(mPath,mPaint);
    }

    /**
     * 绘制二阶贝塞尔曲线
     * @param canvas
     */
    private void quadTo(Canvas canvas) {
        //0,500起点  100,300是数据点  200,500结束点
        mPath.moveTo(0,500);
        mPath.quadTo(100,300,200,500);
        canvas.drawPath(mPath,mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void to2(Canvas canvas) {
        mPath.moveTo(0,0);
        mPath.lineTo(100,100);
        //绘制一个圆弧  startAngle从0度扫过270度  顺时针  forceMoveTo 为true 强制位移 和 line 不做连线  false则连线
        mPath.arcTo(400,200,600,400,0,270,true);
        canvas.drawPath(mPath,mPaint);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void to(Canvas canvas) {
        mPath.addArc(200,200,400,400,-225,225);
        mPath.arcTo(400,200,600,400,-180,225,true);
        canvas.drawPath(mPath,mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void add(Canvas canvas) {
        mPath.addArc(200,200,500,500,0,180);

        //绘制一个矩形  CW是顺时针  CCW是逆时针
        mPath.addRect(new RectF(200,200,500,500),Path.Direction.CW);

        //在中心点 700，700 的地方绘制一只半径为200的圆
        mPath.addCircle(200,200,200,Path.Direction.CW);
        //绘制一个椭圆
        mPath.addOval(new RectF(0,0,300,400),Path.Direction.CW);
        canvas.drawPath(mPath,mPaint);
    }

    private void line(Canvas canvas) {
        //        mPaint.setStyle(Paint.Style.FILL);
        mPath.moveTo(100,70);//移动
        mPath.lineTo(140, 800);//连线
        //等同于上一行代码mPath.rlineTo(40,730)
        mPath.lineTo(250,600);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }
}
