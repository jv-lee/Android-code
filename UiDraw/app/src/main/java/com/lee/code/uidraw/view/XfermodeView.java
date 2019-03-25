package com.lee.code.uidraw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * 图层混合 模式 示例
 */
public class XfermodeView extends View {
    private Paint mPaint;
    private int mWidth,mHeight;
    private static final Xfermode[] sModes = {
            //所绘制不会提交到画布上
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            //显示上层绘制的图像
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            //显示下层绘制的图像
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            //正常绘制显示，上下层绘制重叠
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            //上下层都显示，下层据上显示
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            //取两层绘制交集，显示上层
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            //取两层绘制交集，显示下层
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            //取上层绘制非交集部分，交集部分变成透明
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            //取下层绘制非交集部分，交集部分变成透明
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            //取上层交集部分与下层非交集部分
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            //取下层交集部分与上层非交集部分
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            //去除两图层交集部分
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            //取两图层全部区域，交集部分颜色加深
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            //取两图层全部区域，交集部分颜色点亮
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
    };

    public XfermodeView(Context context) {
        super(context);
        initPaint();
    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public XfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        proterDuffXfermode(canvas);

    }

    /**
     * 图层混合模式
     */
    private void proterDuffXfermode(Canvas canvas){
        //使用图层混合 禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        setBackgroundColor(Color.GRAY);
        //离屏绘制
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        //目标图
        canvas.drawBitmap(createRectBitmap(mWidth,mHeight),0,0,mPaint);
        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //源图，重叠区域右下角部分
        canvas.drawBitmap(createCircleBitmap(mWidth, mHeight), 0, 0, mPaint);
        //清除混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    //画矩形
    public Bitmap createRectBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint dstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dstPaint.setColor(0xFF66AAFF);
        canvas.drawRect(new Rect(width/20,height /3,2*width/3,19*height/20),dstPaint);
        return bitmap;
    }

    //画圆src
    public Bitmap createCircleBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        srcPaint.setColor(Color.RED);
        canvas.drawCircle(width * 2/ 3,height/3,height/4,srcPaint);
        return bitmap;
    }

}
