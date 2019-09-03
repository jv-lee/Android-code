package gionee.gnservice.app.view.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import gionee.gnservice.app.R;

/**
 * @author jv.lee
 * @date 2019/8/5.
 * @description
 */
public class RedRainView extends FrameLayout {
    public RedRainView(Context context) {
        this(context, null);
    }

    public RedRainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedRainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void initIcon() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_redrain, null, false);
        addView(view);

        ImageView ivButton = view.findViewById(R.id.iv_btn);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ivButton.setOnClickListener(v -> {
            if (call != null) {
                call.buttonCall();
            }
        });
        ivClose.setOnClickListener(v -> {
            if (call != null) {
                call.closeCall();
            }
        });

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1F, 0.8F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1F, 0.8F);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ivButton, scaleX, scaleY);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.start();

    }

    private Callback call;

    public void setCallback(Callback call) {
        this.call = call;
    }

    public interface Callback {
        void buttonCall();

        void closeCall();
    }
}
