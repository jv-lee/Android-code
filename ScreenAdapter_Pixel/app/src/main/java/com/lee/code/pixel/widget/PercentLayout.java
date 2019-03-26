package com.lee.code.pixel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lee.code.pixel.R;

/**
 * 百分比布局
 */
public class PercentLayout extends RelativeLayout {
    public PercentLayout(Context context) {
        super(context);
    }

    public PercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取父容器的尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (checkLayoutParams(layoutParams)) {
                LayoutParams lp = (LayoutParams) layoutParams;
                float widthPercent = lp.widthPercent;
                float heightPercent = lp.heightPercent;
                float marginPercent = lp.marginLeftPercent;
                float marginRightPercent = lp.marginRightPercent;
                float marginTopPercent = lp.marginTopPercent;
                float marginBottomPercent = lp.marginBottomPercent;

                //属性换算 通过当前属性设置的百分比 乘以父容器属性的宽高 得出百分比比例 参数值为float 0.1- 1
                if (widthPercent > 0) {
                    layoutParams.width = (int) (widthSize * widthPercent);
                }

                if (heightPercent > 0) {
                    layoutParams.height = (int) (heightSize * heightPercent);
                }

                if (heightPercent > 0) {
                    ((LayoutParams) layoutParams).marginLeftPercent = (int) (widthSize * marginPercent);
                }

                if (heightPercent > 0) {
                    ((LayoutParams) layoutParams).marginRightPercent = (int) (widthSize * marginRightPercent);
                }

                if (heightPercent > 0) {
                    ((LayoutParams) layoutParams).marginTopPercent = (int) (heightSize * marginTopPercent);
                }

                if (heightPercent > 0) {
                    ((LayoutParams) layoutParams).marginBottomPercent = (int) (heightSize * marginBottomPercent);
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 要保持RelativeLayout的属性特性 需继承一个内部类 定义属性
     */
    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

         LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //解析自定义属性
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            widthPercent = typedArray.getFloat(R.styleable.PercentLayout_widthPercent, 0);
            heightPercent = typedArray.getFloat(R.styleable.PercentLayout_heightPercent, 0);
            marginLeftPercent = typedArray.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0);
            marginRightPercent = typedArray.getFloat(R.styleable.PercentLayout_marginRightPercent, 0);
            marginTopPercent = typedArray.getFloat(R.styleable.PercentLayout_marginTopPercent, 0);
            marginBottomPercent = typedArray.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0);
        }
    }

}
