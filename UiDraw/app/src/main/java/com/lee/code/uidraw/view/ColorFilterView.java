package com.lee.code.uidraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;

import com.lee.code.uidraw.R;

public class ColorFilterView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;

    public ColorFilterView(Context context) {
        this(context,null);
    }

    public ColorFilterView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorFilterView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        porterDuffColorFilter(canvas);
//        colorMatrixColorFilter(canvas);
        colorMatrix(canvas);
    }

    /**
     * LightingColorFilter 滤镜
     * 构造方法 ： LightingColorFilter(int mul,int add)
     * 参数：
     * mul 和 add 都是和颜色值格式相同的int值 ，其中　mul用来和目标像素相乘，
     * add用来和目标像素相加：
     * R' = R * mul.R / 0xff + add.R
     * G' = G * mul.G / 0xff + add.G
     * B' = B * mul.B / 0xff + add.B
     * 使用：
     * ColorFilter lighting = new LightingColorFilter(0x00ffff,0x000000);
     * paint.setColorFilter(lighting);
     * canvas.drawBitmap(bitmap,0,0,paint);
     */
    private void lightingColorFilter(Canvas canvas) {


        //红色去除掉
//        LightingColorFilter lighting = new LightingColorFilter(0x00ffff, 0x000000);
//        mPaint.setColorFilter(lighting);
//        canvas.drawBitmap(mBitmap,0,0,mPaint);

        //原始图片效果
//        LightingColorFilter lighting = new LightingColorFilter(0xffffff, 0x000000);
//        mPaint.setColorFilter(lighting);
//        canvas.drawBitmap(mBitmap,0,0,mPaint);

        //绿色更亮
        LightingColorFilter lighting = new LightingColorFilter(0xffffff, 0x003000);
        mPaint.setColorFilter(lighting);
        canvas.drawBitmap(mBitmap,0,0,mPaint);
    }

    /**
     * 只能指定颜色 图层混合效果
     * @param canvas
     */
    private void porterDuffColorFilter(Canvas canvas) {
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
        mPaint.setColorFilter(porterDuffColorFilter);
        canvas.drawBitmap(mBitmap,100,0,mPaint);
    }

    /**
     * 3.ColorMatrixColorFilter滤镜
     * 构造方法：ColorMatrixColorFilter(float[] colorMatrix);
     * 参数：
     * colorMatrix 矩阵数组
     * 使用： 第五列调整颜色的偏移量
     * float[] colorMatrix = {
     *     1,0,0,0,0, //red
     *     0,1,0,0,0, //green
     *     0,0,1,0,0, //blue
     *     0,0,0,1,0 //alpha
     * }
     * mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
     * canvas.drawBitmap(mBitmap,100,0,mPaint);
     * @param canvas
     */
    private void colorMatrixColorFilter(Canvas canvas) {
        float[] colorMatrix = {2,0,0,0,0,
                                0,2,0,0,0,
                                0,0,2,0,0,
                                0,0,0,2,0};
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colormatrix_fanse);
        mPaint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(mBitmap,100,0,mPaint);
    }

    private void colorMatrix(Canvas canvas) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(1,1,1,1);

        //饱和度调节0-无色  1-默认效果 > 1 饱和度增强
        colorMatrix.setSaturation(2);

        //调整红色 的偏移量
        colorMatrix.setRotate(0,45);

        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(mBitmap, 100, 0, mPaint);
    }

    //胶片数组
    public static final float colormatrix_fanse[] = {
            -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
            0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
    };
}
