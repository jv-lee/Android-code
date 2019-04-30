package com.lee.music.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BackgroundAnimationRelativeLayout extends RelativeLayout {
    /**
     * 图层叠加 实现高斯模糊渐变
     */
    private LayerDrawable layerDrawable;

    /**
     * 视图的属性动画
     */
    private ObjectAnimator objectAnimator;


    public BackgroundAnimationRelativeLayout(Context context) {
        super(context);
    }

    public BackgroundAnimationRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundAnimationRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        
    }
}
