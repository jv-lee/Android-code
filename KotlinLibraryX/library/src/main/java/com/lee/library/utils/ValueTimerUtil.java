package com.lee.library.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * @author jv.lee
 * @date 2019/9/16.
 * @description ValueAnimation 时间统计工具类
 */
@androidx.annotation.RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ValueTimerUtil {

    public static ValueAnimator changeRepeat(int time, TimeCallback callback) {
        ValueAnimator value = ValueAnimator.ofInt(1, time);
        value.setDuration(time);
        value.setRepeatCount(Animation.INFINITE);
        value.setInterpolator(new LinearInterpolator());
        value.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if (callback != null) {
                    callback.notification();
                }
            }
        });
        return value;
    }

    public static ValueAnimator changeEnd(int time, TimeCallback callback) {
        ValueAnimator value = ValueAnimator.ofInt(1, time);
        value.setDuration(time);
        value.setRepeatCount(Animation.INFINITE);
        value.setInterpolator(new LinearInterpolator());
        value.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (callback != null) {
                    callback.notification();
                }
            }
        });
        return value;
    }

    public static void resume(ValueAnimator value) {
        if (value != null) {
            if (!value.isRunning()) {
                value.start();
                return;
            }
            if (value.isPaused()) {
                value.resume();
            }
        }
    }

    public static void pause(ValueAnimator value) {
        if (value != null) {
            if (value.isRunning()) {
                value.pause();
            }
        }
    }

    public static void destroy(ValueAnimator value) {
        if (value != null) {
            value.pause();
            value.end();
            value.cancel();
        }
    }

    public interface TimeCallback {
        void notification();
    }

}
