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
    private Path mPath2;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPath2 = new Path();
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
     *
     * @param canvas
     */
    private void cubicTo(Canvas canvas) {
        //起点0,100  第一个数据点100,200
        mPath.moveTo(0, 100);
        mPath.cubicTo(100, 200, 200, 0, 300, 200);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制二阶贝塞尔曲线
     *
     * @param canvas
     */
    private void quadTo(Canvas canvas) {
        //0,500起点  100,300是数据点  200,500结束点
        mPath.moveTo(0, 500);
        mPath.quadTo(100, 300, 200, 500);
        canvas.drawPath(mPath, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void to2(Canvas canvas) {
        mPath.moveTo(0, 0);
        mPath.lineTo(100, 100);
        //绘制一个圆弧  startAngle从0度扫过270度  顺时针  forceMoveTo 为true 强制位移 和 line 不做连线  false则连线
        mPath.arcTo(400, 200, 600, 400, 0, 270, true);
        canvas.drawPath(mPath, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void to(Canvas canvas) {
        mPath.addArc(200, 200, 400, 400, -225, 225);
        mPath.arcTo(400, 200, 600, 400, -180, 225, true);
        canvas.drawPath(mPath, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void add(Canvas canvas) {
        mPath.addArc(200, 200, 500, 500, 0, 180);

        //绘制一个矩形  CW是顺时针  CCW是逆时针
        mPath.addRect(new RectF(200, 200, 500, 500), Path.Direction.CW);

        //在中心点 700，700 的地方绘制一只半径为200的圆
        mPath.addCircle(200, 200, 200, Path.Direction.CW);
        //绘制一个椭圆
        mPath.addOval(new RectF(0, 0, 300, 400), Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
    }

    private void line(Canvas canvas) {
        //{mPaint.setStyle(Paint.Style.FILL);}
        //移动
        mPath.moveTo(100, 70);
        //连线
        mPath.lineTo(140, 800);
        //等同于上一行代码mPath.rlineTo(40,730)
        mPath.lineTo(250, 600);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * Path相加设置模式
     *
     * @param canvas
     */
    private void drawOp(Canvas canvas) {
        mPath.addCircle(200, 200, 100, Path.Direction.CW);
        mPath2.addCircle(300, 300, 100, Path.Direction.CW);
        //路径1 减去 路径2
        mPath.op(mPath2, Path.Op.DIFFERENCE);
        //路径1 与 路径2交集
        mPath.op(mPath2, Path.Op.INTERSECT);
        //路径1 加上 路径2
        mPath.op(mPath2, Path.Op.UNION);
        //路径1 与 路径2不相交的部分
        mPath.op(mPath2, Path.Op.XOR);
        //路径2 减去 路径1
        mPath.op(mPath2, Path.Op.REVERSE_DIFFERENCE);

        canvas.drawPath(mPath, mPaint);
    }

    private void drawFillType(Canvas canvas) {
        mPath.addCircle(200, 200, 100, Path.Direction.CW);
        mPath.addCircle(300, 300, 100, Path.Direction.CW);

        //路径1与路径2不相交部分
        mPath.setFillType(Path.FillType.EVEN_ODD);
        //反向绘制路径区域外
        mPath.setFillType(Path.FillType.INVERSE_WINDING);
        //绘制相交区域及外部区域
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(mPath, mPaint);
    }

}
