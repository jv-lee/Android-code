package com.lee.music.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形水波纹view
 * @author jv.lee
 * @date 2019/5/2
 */
public class RippleCircleView extends View {

    private RippleAnimationView rippleAnimationView;

    public RippleCircleView(RippleAnimationView rippleAnimationView) {
        super(rippleAnimationView.getContext());
        this.rippleAnimationView = rippleAnimationView;
        this.setVisibility(INVISIBLE);
    }

    public RippleCircleView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleCircleView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth(), getHeight()) / 2;
        //画圆形水波纹
        canvas.drawCircle(radius,radius,radius-rippleAnimationView.getStrokeWidth(),rippleAnimationView.getmPaint());
    }
}
