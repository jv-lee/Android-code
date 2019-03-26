package com.lee.code.pixel.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ScreenAdapterLayout extends RelativeLayout {

    private boolean flag;

    public ScreenAdapterLayout(Context context) {
        super(context);
    }

    public ScreenAdapterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenAdapterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!flag) {
            float widthScale = PixelUtils.getInstance(getContext()).getHorizontalScale();
            float heightScale = PixelUtils.getInstance(getContext()).getVerticalScale();

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.width = (int) (layoutParams.width * widthScale);
                layoutParams.height = (int) (layoutParams.height * heightScale);
                layoutParams.topMargin = (int) (layoutParams.topMargin * heightScale);
                layoutParams.rightMargin = (int) (layoutParams.rightMargin * widthScale);
                layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * heightScale);
                layoutParams.leftMargin = (int) (layoutParams.leftMargin * widthScale);
            }
            flag = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
