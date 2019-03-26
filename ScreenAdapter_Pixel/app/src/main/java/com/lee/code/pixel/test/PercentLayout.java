package com.lee.code.pixel.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lee.code.pixel.R;

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
        //获取父容器size
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (checkLayoutParams(layoutParams)) {
                LayoutParams lp = (LayoutParams) layoutParams;
                if (lp.layout_widthPercent > 0) {
                    lp.width = (int) (widthSize * lp.layout_widthPercent);
                }
                if (lp.layout_heightPercent > 0) {
                    lp.height = (int) (heightSize * lp.layout_heightPercent);
                }
                if (lp.layout_marginTopPercent > 0) {
                    lp.topMargin = (int) (heightSize * lp.layout_marginTopPercent);
                }
                if (lp.layout_marginRightPercent > 0) {
                    lp.rightMargin = (int) (widthSize * lp.layout_marginRightPercent);
                }
                if (lp.layout_marginBottomPercent > 0) {
                    lp.bottomMargin = (int) (heightSize * lp.layout_marginBottomPercent);
                }
                if (lp.layout_marginLeftPercent > 0) {
                    lp.leftMargin = (int) (widthSize * lp.layout_marginLeftPercent);
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

    public static class LayoutParams extends RelativeLayout.LayoutParams{
        private float layout_widthPercent;
        private float layout_heightPercent;
        private float layout_marginTopPercent;
        private float layout_marginRightPercent;
        private float layout_marginBottomPercent;
        private float layout_marginLeftPercent;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            layout_widthPercent = typedArray.getFloat(R.styleable.PercentLayout_widthPercent,0);
            layout_heightPercent = typedArray.getFloat(R.styleable.PercentLayout_heightPercent, 0);
            layout_marginTopPercent = typedArray.getFloat(R.styleable.PercentLayout_marginTopPercent, 0);
            layout_marginRightPercent = typedArray.getFloat(R.styleable.PercentLayout_marginRightPercent,0);
            layout_marginBottomPercent = typedArray.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0);
            layout_marginLeftPercent = typedArray.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0);
        }
    }
}
