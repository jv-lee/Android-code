package com.lee.code.uidraw.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jv.lee
 * @date 2019/10/8.
 * @description Paint Api调用示例
 */
public class PaintView extends View {

    private Paint mPaint;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * paint基础api
     */
    private void init() {
        mPaint = new Paint();//初始化
        mPaint.setColor(Color.RED);//设置颜色
        mPaint.setARGB(255, 0, 0, 0);//设置Paint对象颜色 ，范围为0-255
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
