package com.lee.code.uidraw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
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

    /**
     * paint基础api
     */
    private void init() {
        mPaint = new Paint();//初始化
        mPaint.setColor(Color.RED);//设置颜色
        mPaint.setARGB(255,0,0,0);//设置Paint对象颜色 ，范围为0-255
        mPaint.setAlpha(200);//设置alpha透明度，范围为 0-255
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//描边效果 描边STROKE  实心FILL
        mPaint.setStrokeWidth(4);//描边宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//圆角效果  BUTT默认状态  ROUND圆角效果  SQUARE方角效果  方角比默认长一点 因为是添加的方角
        mPaint.setStrokeJoin(Paint.Join.MITER);//拐角效果  MITER默认尖角   BEVEL 平角类型   ROUND圆角类型
        mPaint.setShader(new SweepGradient(200, 200, Color.BLUE, Color.RED));//设置环形渲染器
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));//设置涂层混合模式
        mPaint.setColorFilter(new LightingColorFilter(0x00ffff, 0x000000));//设置颜色过滤器　
        mPaint.setFilterBitmap(true);//设置双线性过滤  设置完后 图片的效果会更加平滑  比不设置的马赛克效果好
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));//设置画笔遮罩滤镜，传入度数和样式
        mPaint.setTextScaleX(2);//设置文本缩放倍数
        mPaint.setTextSize(38);//设置字体大小
        mPaint.setTextAlign(Paint.Align.LEFT);//对齐方式
        mPaint.setUnderlineText(true);//设置下划线

        String str = "Android高级工程师";
        Rect rect = new Rect();
        mPaint.getTextBounds(str, 0, str.length(), rect);//测量文本大小，将文本大小信息存放在rect中
        mPaint.measureText(str);
        mPaint.getFontMetrics();//获取字体度量对象

    }
}
