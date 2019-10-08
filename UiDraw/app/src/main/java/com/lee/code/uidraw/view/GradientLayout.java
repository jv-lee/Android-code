package com.lee.code.uidraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.lee.code.uidraw.R;

/**
 * 渲染器Shader 示例
 */
public class GradientLayout extends View {
    private Paint mPaint;
    private Shader mShader;
    private Bitmap mBitmap;


    public GradientLayout(Context context) {
        super(context,null);
        initPaint();
    }

    /**
     * 通过布局文件声明
     * @param context
     * @param attrs
     */
    public GradientLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        initPaint();
    }

    public GradientLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        linearGradient(canvas);
//        radialGradient(canvas);
//        sweepGradient(canvas);
        bitmapShader(canvas);
    }

    private void initPaint(){
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * 线性渲染器
     *
     */
    private void linearGradient(Canvas canvas) {
        /**
         * 1.线性渲染：LinearGradient(float x0,float y0,float x1,float y1,int colors[],float positions[], @NonNull TileMode tile)
         *  (x0,y0) ：渐变起始点坐标
         *  (x1,y1) ：渐变结束点坐标
         *  colors  : 渐变颜色数组 开始颜色-结束颜色 可为多个
         *  position: 颜色占比比例 0.1f - 1  根据颜色的个数同步 传null 则为线性化渐变
         *  tile    : 用于指定控件区域大于追定的渐变区域时，空白区域的颜色填充方法
         */
        mShader = new LinearGradient(0, 0, 500, 500, new int[]{Color.RED, Color.BLUE}, new float[]{0.5f,1}, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        canvas.drawRect(0,0,500,500,mPaint);
    }

    /**
     * 2.环形渲染器: RadialGradient(float centerX, float centerY, float radius,int colors[],float stops[],TileMode tileMode)
     *  centerX,centerY:表示shader的中心坐标，开始渐变的坐标
     *  radius:渐变的半径 画圆
     *  colors[]: 中心渐变颜色-边界渐变颜色 可为多个
     *  stops[] : 颜色占比 从中心依次到边界  根据colors的个数同步
     *  tileMode:shader未覆盖以外的填充模式
     */
    private void radialGradient(Canvas canvas){
        mShader = new RadialGradient(250,250,250,new int[]{Color.RED,Color.BLUE},new float[]{0.3f,1},Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        canvas.drawCircle(250,250,250,mPaint);

    }

    /**
     * 3.扫描渲染：SweepGradient(float cx, float cy, @ColorInt int color0, @ColorInt int color1)
     * cx,cy :扫描渐变中心坐标
     * color0-colorX /  colors[]  扫描渐变颜色，
     * positions[] : 扫描渐变如果有colors[] 根据每个颜色设置占比 .0 - 1
     * @param canvas
     */
    private void sweepGradient(Canvas canvas) {
        mShader = new SweepGradient(250, 250, Color.RED, Color.BLUE);
        mPaint.setShader(mShader);
        canvas.drawCircle(250,250,250,mPaint);
    }

    /**
     * 4.位图渲染 BitmapShader(Bitmap bitmap,TileMode tileX，TileMode tileY)
     * bitmap : 构造的shader 使用的bitmap
     * tileX : X轴方向的 TileMode
     * tileY : Y轴方向的 TileMode
     * TileMode.REPEAT 绘制区域超过渲染区域的部分，重复排版
     * TileMode.CLAMP 绘制区域超过渲染区域的部分，会以最后一个像素拉伸排版
     * TileMode.MIRROR 绘制区域超过渲染区域的部分，镜像翻转排版
     * @param canvas
     */
    private void bitmapShader(Canvas canvas){
        mShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        canvas.drawCircle(800,800,800,mPaint);
        canvas.drawRect(0,0,800,800,mPaint);
    }

    /**
     * 5.组合渲染 ComposeShader()
     * 渲染层叠
     * @param canvas
     */
    private void composeShader(Canvas canvas) {
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        LinearGradient linearGradient = new LinearGradient(0,0,1000,1600,new int[]{Color.RED,Color.GREEN,Color.BLUE},null,Shader.TileMode.CLAMP);
        mShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(mShader);
        canvas.drawCircle(250,250,250,mPaint);
    }
}
