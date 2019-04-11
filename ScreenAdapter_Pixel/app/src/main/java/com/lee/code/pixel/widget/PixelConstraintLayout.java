package com.lee.code.pixel.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lee.code.pixel.utils.PixelUtils;

/**
 * 像素适配布局ConstraintLayout
 * @author jv.lee
 * @date 2019/4/11
 */
public class PixelConstraintLayout extends ConstraintLayout {

    private boolean flag;

    public PixelConstraintLayout(Context context) {
        super(context);
    }

    public PixelConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PixelConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //只换算一次
        if (!flag) {
            float scaleX = PixelUtils.getInstance(getContext()).getHorizontalScale();
            float scaleY = PixelUtils.getInstance(getContext()).getVerticalScale();

            //设置子View 布局比例
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                params.width = (int) (params.width * scaleX);
                params.height = (int) (params.height * scaleY);

                params.leftMargin = (int) (params.leftMargin * scaleX);
                params.rightMargin = (int) (params.rightMargin * scaleX);

                params.topMargin = (int) (params.topMargin * scaleY);
                params.bottomMargin = (int) (params.bottomMargin * scaleY);

                if (!(child instanceof ViewGroup)) {
                    int top = (int) (child.getPaddingTop() * scaleY);
                    int right = (int) (child.getPaddingRight() * scaleX);
                    int bottom = (int) (child.getPaddingBottom() * scaleY);
                    int left = (int) (child.getPaddingLeft() * scaleX);
                    child.setPadding(left, top, right, bottom);
                }
            }
            int top = (int) (getPaddingTop() * scaleY);
            int right = (int) (getPaddingRight() * scaleX);
            int bottom = (int) (getPaddingBottom() * scaleY);
            int left = (int) (getPaddingLeft() * scaleX);
            this.setPadding(left,top,right,bottom);
            flag = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
