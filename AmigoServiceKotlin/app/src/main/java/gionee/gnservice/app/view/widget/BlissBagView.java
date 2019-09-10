package gionee.gnservice.app.view.widget;

import android.animation.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import gionee.gnservice.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/9/9.
 * @description
 */
public class BlissBagView extends FrameLayout {

    private ImageView ivImage;
    private ImageView ivBackground;
    private ImageView ivActive;
    private TextView tvMessage;
    private TextView tvCount;
    private FrameLayout frameEgg;

    ObjectAnimator ivAnimator;
    ObjectAnimator bgAnimator;
    ObjectAnimator tvAnimator;
    ValueAnimator valueAnimator;
    List<ObjectAnimator> animators = new ArrayList<>();

    private int award = 0;
    private int insert = 0;
    private boolean hasJump;

    public BlissBagView(Context context) {
        this(context, null);
    }

    public BlissBagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlissBagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_blissbag_view, null);
        addView(view);

        ivImage = view.findViewById(R.id.iv_image);
        ivBackground = view.findViewById(R.id.iv_background);
        ivActive = view.findViewById(R.id.iv_active);
        tvMessage = view.findViewById(R.id.tv_message);
        tvCount = view.findViewById(R.id.tv_count);
        frameEgg = view.findViewById(R.id.frame_egg);

        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1F, 1F, 1.1F, 1F, 1.1F, 1F, 1.1F, 1F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9F, 1F, 0.9F, 1F, 0.9F, 1F, 0.9F, 1F);
        ivAnimator = ObjectAnimator.ofPropertyValuesHolder(ivImage, scaleX, scaleY);
        ivAnimator.setInterpolator(new LinearInterpolator());
        ivAnimator.setDuration(600);

        bgAnimator = ObjectAnimator.ofFloat(ivBackground, View.ALPHA, 0F, 0.0F, 0.0F, 0.3F, 0.6F, 1.0F, 0.9F, 0.8F, 0.7F, 0.5F, 0.0F);
        bgAnimator.setInterpolator(new LinearInterpolator());
        bgAnimator.setStartDelay(400);
        bgAnimator.setDuration(1000);

        PropertyValuesHolder transY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -50, -150);
        PropertyValuesHolder alpha2 = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1, 0);
        tvAnimator = ObjectAnimator.ofPropertyValuesHolder(tvMessage, transY, alpha2);
        tvAnimator.setInterpolator(new LinearInterpolator());
        tvAnimator.setStartDelay(400);
        tvAnimator.setDuration(1000);

        int time = 75;
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 0F);
        for (int i = 0; i < frameEgg.getChildCount(); i++) {
            ImageView imageView = (ImageView) frameEgg.getChildAt(i);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, translationY, alpha);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(2);
            animator.setDuration(time);
            switch (i) {
                case 0:
                    animator.setStartDelay(0);
                    break;
                case 1:
                    animator.setStartDelay(time * 3);
                    break;
                case 2:
                    animator.setStartDelay(time);
                    break;
                case 3:
                    animator.setStartDelay(time * 2);
                    break;
                default:
            }
            animators.add(animator);
        }
    }

    private void startAnimator() {
        if (!ivAnimator.isRunning()) {
            ivAnimator.start();
        }
        if (!bgAnimator.isRunning()) {
            bgAnimator.start();
        }
        if (!tvAnimator.isRunning()) {
            tvAnimator.start();
        }
        for (ObjectAnimator animator : animators) {
            if (!animator.isRunning()) {
                animator.start();
            }
        }
    }

    private void startNumberAnimator() {
        valueAnimator = ValueAnimator.ofInt(award, award + insert);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tvCount.setText(animation.getAnimatedValue().toString());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                award = award + insert;
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setStartDelay(400);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    /**
     * 设置初始鱼蛋显示
     *
     * @param award
     */
    public void setCurrentAward(int award) {
        this.award = award;
        tvCount.setText(String.valueOf(award));
        if (award >= 10000) {
            hasWithdraw(true);
        } else {
            hasWithdraw(false);
        }
    }

    /**
     * 设置是否可提现
     *
     * @param isWithdraw
     */
    public void hasWithdraw(boolean isWithdraw) {
        this.hasJump = isWithdraw;
        if (ivActive != null) {
            ivActive.setVisibility(isWithdraw ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置获取奖励 提示效果
     *
     * @param award
     */
    @SuppressLint("SetTextI18n")
    public void notificationAward(int award) {
        this.insert = award;
        tvMessage.setText("+" + insert);
        startAnimator();
        startNumberAnimator();
        if ((this.award + this.insert) >= 10000) {
            hasWithdraw(true);
        } else {
            hasWithdraw(false);
        }
    }

    public boolean isHasJump() {
        return hasJump;
    }
}
