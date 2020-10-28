package com.lee.library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

/**
 * @author jv.lee
 * @date 2017/10/13
 */

public class ActivityUtil {
    private static ActivityUtil instance;

    private ActivityUtil() {
    }

    public static ActivityUtil getInstance() {
        if (instance == null) {
            synchronized (ActivityUtil.class) {
                if (instance == null) {
                    instance = new ActivityUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 声明一个集合用于记录所有打开的活动
     */
    private ArrayList<Activity> list = new ArrayList<Activity>();

    /**
     * 加入活动对象--------->onCreate
     *
     * @param activity
     */
    public void add(Activity activity) {
        list.add(activity);
    }

    /**
     * 移除活动对象--------->onDestroy
     *
     * @param activity
     */
    public void remove(Activity activity) {
        list.remove(activity);
    }

    /**
     * 关闭所有的活动--------->close
     */
    public void removeAll() {
        for (Activity activity : list) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static boolean assertActivityDestroyed(Context context) {
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                return true;
            }
            if (((Activity) context).isFinishing()) {
                return true;
            }
        }
        return false;
    }
}
