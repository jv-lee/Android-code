package gionee.gnservice.app.view.widget;

import android.animation.*;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import gionee.gnservice.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jv.lee
 * @date 2019/8/1.
 * @description
 */
public class RedRainLayout extends FrameLayout implements View.OnClickListener {

    private int mWidth;
    private int mHeight;

    /**
     * 设置红包数量
     */
    private int count = 30;
    /**
     * 设置红包大小 基数
     */
    private int imageSizeCode = 20;
    /**
     * 红包大小
     */
    private int imageSize = 0;

    private boolean isClick;

    private List<ObjectAnimator> animators = new ArrayList<>();
    private int resourceID = R.mipmap.ic_redrain_icon;

    public RedRainLayout(Context context) {
        this(context, null);
    }

    public RedRainLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedRainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initParams();
        initViews();
    }

    private void initParams() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        imageSize = mWidth / imageSizeCode;
    }

    private void initViews() {
        for (int i = 0; i < count; i++) {
            ImageView image = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(imageSize, imageSize);
            layoutParams.leftMargin = getRandom(0, mWidth);
            image.setAlpha(0f);
            image.setLayoutParams(layoutParams);
            image.setImageResource(resourceID);
            bindAnimator(image);
            addView(image);
        }
        setOnClickListener(this);
    }


    private void bindAnimator(final View iv) {
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, 360F);
//        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", -imageSize, getRandom(mHeight / 2, (int) (mHeight * 0.8)));
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", -imageSize, getRandom((int) (mHeight * 0.8), mHeight));
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 3F, 0.6F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 3F, 0.6F);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1F, 0F);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(iv, rotation, translationY, scaleX, scaleY, alpha);
        animator.setStartDelay(getRandom(0, 2000));
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(Animation.INFINITE);
        animators.add(animator);
    }

    public void stopAnimators() {
        pauseAnimators();
        removeAllViews();
        startOpenRed();
    }

    public void startAnimators() {
        ValueAnimator value = ValueAnimator.ofInt(0, 1);
        value.setDuration(500);
        value.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for (ObjectAnimator animator : animators) {
                    animator.start();
                }
                value.cancel();
            }
        });
        value.start();
    }

    public void resumeAnimators() {
        for (ObjectAnimator animator : animators) {
            if (animator.isPaused()) {
                animator.resume();
            }
        }
    }

    public void pauseAnimators() {
        for (ObjectAnimator animator : animators) {
            if (animator.isRunning()) {
                animator.pause();
            }
        }
    }

    public void destroyAnimators() {
        for (ObjectAnimator animator : animators) {
            if (animator != null) {
                animator.cancel();
            }
        }
    }

    private void startOpenRed() {
        RedRainView imageView = new RedRainView(getContext());

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.initIcon();
        addView(imageView);
        if (callback != null) {
            imageView.setCallback(callback);
        }

//        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", 0, 1080F);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.1F, 1F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.1F, 1F);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, scaleX, scaleY);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

    }

    public void setImageResource(int resourceID) {
        this.resourceID = resourceID;
    }

    public void setRedCount(int count) {
        this.count = count;
    }


    private int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    @Override
    public void onClick(View v) {
        if (!isClick) {
            stopAnimators();
            isClick = true;
        }

    }

    private RedRainView.Callback callback;

    public void setCallback(RedRainView.Callback callback) {
        this.callback = callback;
    }

}
