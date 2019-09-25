package com.lee.code.uidraw.widget.tree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author jv.lee
 * @date 2019/9/25.
 * @description
 */
public class TreeView extends View {

    /**
     * 存储当前需要绘制的树枝
     */
    private LinkedList<Branch> growingBranches;
    private Paint mPaint;
    private Canvas treeCanvas;
    private Bitmap treeBitmap;

    public TreeView(Context context) {
        this(context, null);
    }

    public TreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(100);
        mPaint.setColor(0xFF000000);
        growingBranches = new LinkedList<>();
        growingBranches.add(getBranches());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //绘制的内容如何保存进入Bitmap
        treeBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //treeCanvas绘制的内容都会保存在Bitmap当中
        treeCanvas = new Canvas(treeBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBranches();
        canvas.drawBitmap(treeBitmap, 0, 0, mPaint);
    }

    /**
     * 绘制树枝
     */
    private void drawBranches() {
        //绘制之前先要获取到此次绘制需要的树枝
        if (!growingBranches.isEmpty()) {
            //需要继续绘制
            treeCanvas.save();
            //移动到中间， 底部开始绘制
            treeCanvas.translate(getWidth() / 2 - 217, getHeight() - 490);
            LinkedList<Branch> tempBranches = null;
            Iterator<Branch> iterator = growingBranches.iterator();
            while (iterator.hasNext()) {
                Branch branch = iterator.next();
                if (!branch.grow(treeCanvas, mPaint, 1)) {
                    iterator.remove();
                    //是否有分支需要绘制
                    if (branch.childList != null) {
                        if (tempBranches == null) {
                            tempBranches = branch.childList;
                        } else {
                            tempBranches.addAll(branch.childList);
                        }
                    }
                }
            }

            treeCanvas.restore();
            if (tempBranches != null) {
                growingBranches.addAll(tempBranches);
            }
        }
        if (!growingBranches.isEmpty()) {
            //继续绘制
            invalidate();
        }

    }

    private Branch getBranches() {
        //data: id,parentId,bezier,control,points(3 points,in 6 columns),max radius, length
        int[][] data = new int[][]{
                {0, -1, 217, 490, 252, 60, 182, 10, 30, 100},
                {1, 0, 222, 310, 137, 227, 22, 210, 13, 100},
                {2, 1, 132, 245, 116, 240, 76, 205, 2, 40},
                {3, 0, 232, 255, 282, 166, 362, 155, 12, 100},
                {4, 3, 260, 210, 330, 219, 343, 236, 3, 80},
                {5, 0, 217, 91, 219, 58, 216, 27, 3, 40},
                {6, 0, 228, 207, 95, 57, 10, 54, 9, 80},
                {7, 6, 109, 96, 65, 63, 53, 15, 2, 40},
                {8, 6, 180, 155, 117, 125, 77, 140, 4, 60},
                {9, 0, 228, 167, 290, 62, 360, 31, 6, 100},
                {10, 9, 272, 103, 328, 87, 330, 81, 2, 80}
        };
        int length = data.length;
        Branch[] branches = new Branch[length];
        for (int i = 0; i < length; i++) {
            branches[i] = new Branch(data[i]);
            int parentId = data[i][1];
            //当前不是主分支
            if (parentId != -1) {
                branches[parentId].addChild(branches[i]);
            }
        }
        //返回主干
        return branches[0];
    }
}
