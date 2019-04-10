package com.lee.code.uidraw.widget.map;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * @author jv.lee
 * @date 2019/4/10
 */
public class ProviceItem {
    private Path path;

    /**
     * 绘制颜色
     */
    private int drawColor;

    public ProviceItem(Path path) {
        this.path = path;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        //是否选中
        if (isSelect) {
            //绘制内部区域与颜色
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);

            //绘制边界
            paint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFD0E8F4;
            paint.setColor(strokeColor);
            canvas.drawPath(path, paint);

        }else{

        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(0,0,0,0xffffff);
        canvas.drawPath(path,paint);

        //绘制边界
        paint.clearShadowLayer();
        paint.setColor(drawColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        canvas.drawPath(path,paint);
        }
    }

    /**
     * 是否点击的位置是path绘制的省份地区内
     * @param x
     * @param y
     * @return
     */
    public boolean isTouch(float x, float y) {
        //首先绘制一个矩形
        RectF rectF = new RectF();
        //通过path.computeBounds方法 获得path路径的最大范围的矩形
        path.computeBounds(rectF,true);
        //通过region放入一个最大矩形的上下左右位置 获得一个最大范围的坐标系统
        Region region = new Region();

        //获得path在最大矩形中的位置
        region.setPath(path, new Region((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.right));

        //判断当前点击位置 是否是 这个最大矩形的位置内的path范围内
        return region.contains((int) x, (int) y);
    }
}
