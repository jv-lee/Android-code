package com.lee.music.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.lee.music.R;

/**
 * 大背景控件
 * @author jv.lee
 */
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
        this(context,null);
    }

    public BackgroundAnimationRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BackgroundAnimationRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Drawable backgroundDrawable = getContext().getApplicationContext().getDrawable(R.drawable.ic_blackground);
        Drawable[] drawables = new Drawable[2];

        //初始化时先将前景与背景颜色设为一致
        drawables[0] = backgroundDrawable;
        drawables[1] = backgroundDrawable;
        layerDrawable = new LayerDrawable(drawables);

        objectAnimator = ObjectAnimator.ofFloat(this, "number", 0f, 1.0f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //根据 0开始 1结束
                int foregroundAlpha = (int) ((float)animation.getAnimatedValue() * 255);
                layerDrawable.getDrawable(1).setAlpha(foregroundAlpha);
                setBackground(layerDrawable);
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后，将原来的背景图更新
                layerDrawable.setDrawable(0,layerDrawable.getDrawable(1));
            }
        });
    }

    @Override
    public void setForeground(Drawable drawable) {
        layerDrawable.setDrawable(1,drawable);
        objectAnimator.start();
    }

}
