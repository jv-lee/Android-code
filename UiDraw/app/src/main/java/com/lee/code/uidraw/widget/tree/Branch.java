package com.lee.code.uidraw.widget.tree;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.LinkedList;

/**
 * @author jv.lee
 * @date 2019/9/25.
 * @description 树枝
 */
public class Branch {
    public static int branchColor = 0xFFFF9988;

    /**
     * 控制点
     */
    private PointF[] cp = new PointF[3];
    private int currentLength;
    private int maxLength;
    private float radius;
    /**
     * 总绘制份数
     */
    private float part;
    private float growX;
    private float growY;

    /**
     * 存储分支
     */
    LinkedList<Branch> childList;

    public Branch(int data[]) {
        //data: id,parentId,bezier,control,points(3 points,in 6 columns),max radius, length
        //{0,-1,217,490,252,60,182,10,30,100}
        cp[0] = new PointF(data[2], data[3]);
        cp[1] = new PointF(data[4], data[5]);
        cp[2] = new PointF(data[6], data[7]);
        radius = data[8];
        maxLength = data[9];
        part = 1f / maxLength;
    }

    public void addChild(Branch branch) {
        if (childList == null) {
            childList = new LinkedList<>();
        }
        childList.add(branch);
    }

    public boolean grow(Canvas treeCanvas, Paint mPaint, int scaleFraction) {
        //需要绘制当前的树枝
        if (currentLength < maxLength) {
            //计算当前绘制的点
            bezier(part * currentLength);
            draw(treeCanvas, mPaint, scaleFraction);
            currentLength++;
            radius *= 0.97f;
            return true;
        } else {
            return false;
        }

    }

    private void draw(Canvas treeCanvas, Paint mPaint, int scaleFraction) {
        mPaint.setColor(branchColor);
        //保存画布状态后， 开始操作缩放、平移操作
        treeCanvas.save();
        treeCanvas.scale(scaleFraction, scaleFraction);
        treeCanvas.translate(growX, growY);
        treeCanvas.drawCircle(0, 0, radius, mPaint);
        //恢复缩放、平移操作之前的画布状态
        treeCanvas.restore();
    }

    private void bezier(float t) {
        //通过贝塞尔曲线公式计算当前的点
        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;
        growX = c0 * cp[0].x + c1 * cp[1].x + c2 * cp[2].x;
        growY = c0 * cp[0].y + c1 * cp[1].y + c2 * cp[2].y;
    }
}
