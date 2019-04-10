package com.lee.code.uidraw.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.lee.code.uidraw.R;

/**
 * @author jv.lee
 * @date 2019/4/9
 */
public class CarView extends View {
    private Bitmap carBitmap;

    private Path path;
    /**
     * 路径计算
     */
    private PathMeasure pathMeasure;
    private float distanceRatio = 0;
    /**
     * 画圆圈的画笔
     */
    private Paint circlePaint;
    /**
     * 画小车的画笔
     */
    private Paint carPaint;

    /**
     * 针对car bitmap图片操作的矩阵
     */
    private Matrix carMatrix;

    public CarView(Context context) {
        this(context,null);
    }

    public CarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        carBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        path = new Path();
        path.addCircle(0,0,200,Path.Direction.CW);
        pathMeasure = new PathMeasure(path, false);

        //绘制圆环
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLACK);

        //绘制绕远旋转图
        carPaint = new Paint();
        carPaint.setColor(Color.DKGRAY);
        carPaint.setStrokeWidth(2);
        carPaint.setStyle(Paint.Style.STROKE);

        carMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //移动到canvas中心点
        canvas.translate(width/2,height/2);
        carMatrix.reset();//置空


        distanceRatio += 0.006;
        if (distanceRatio >= 1) {
            distanceRatio = 0;
        }

        float[] pos = new float[2];
        float[] tan = new float[2];
        float distance = pathMeasure.getLength() * distanceRatio;
        pathMeasure.getPosTan(distance,pos,tan);
        //tan0代表 cos tan[1] sin  //计算小车本身要转转的角度
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        //设置旋转角度和大小
        carMatrix.postRotate(degree,carBitmap.getWidth()/2,carBitmap.getHeight()/2);
        //这里要将设置到小车的中心点
        carMatrix.postTranslate(pos[0] - carBitmap.getWidth() / 2, pos[1] - carBitmap.getHeight() / 2);
        canvas.drawPath(path,circlePaint);
        canvas.drawBitmap(carBitmap,carMatrix,carPaint);
        invalidate();
    }
}
