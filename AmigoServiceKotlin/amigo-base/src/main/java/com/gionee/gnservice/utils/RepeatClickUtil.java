package com.gionee.gnservice.utils;

import android.view.View;

public final class RepeatClickUtil {
    private static final long THRESHOLD = 1000l; // 1s

    private RepeatClickUtil() {
        throw new RuntimeException("Stub");
    }


    public static boolean canRepeatClick(View view) {
        return canRepeatClick(view, THRESHOLD);
    }

    public static boolean canRepeatClick(View view, long threshold) {
        return canRepeatClick(view, view.getId(), threshold);
    }

    public static boolean canRepeatClick(View view, int key, long threshold) {
        Object tag = view.getTag(key);
        long currentTime = System.currentTimeMillis();
        if (tag != null && tag instanceof Long) {
            long lastClickTime = (Long) tag;
            long intervalTime = Math.abs(currentTime - lastClickTime);
            boolean validClick = intervalTime > threshold;
            if (validClick) {
                view.setTag(key, currentTime);
                return true;
            } else {
                return false;
            }
        }

        view.setTag(key, currentTime);
        return true;
    }
}
