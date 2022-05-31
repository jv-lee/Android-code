package com.lee.library.utils;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 *
 * @author jv.lee
 */
public class DensityUtil {

    /**
     * 参考设备的宽，单位：dp
     */
    private static final float WIDTH = 360;

    /**
     * 表示屏幕密度
     */
    private static float appDensity;
    /**
     * 字体的缩放比例，默认appDensity
     */
    private static float appScaleDensity;

    private static HashMap<String, ComponentCallbacks> mComponentCallbacks;

    /**
     * 修改当前activity的缩放比例 调整dpi值
     * 单Fragment架构在onStart中调用 、 多Activity架构在onCreate中调用
     *
     * @param activity 需要设置的activity
     */
    public static void setDensity(Activity activity) {
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics = activity.getApplication().getResources().getDisplayMetrics();
        //计算目标值 density,scaledDensity,densityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH;

        if (appDensity == 0) {
            //初始化赋值操作
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            if (mComponentCallbacks == null) {
                mComponentCallbacks = new HashMap<>();
            }

            //添加字体变化监听回调
            if (!mComponentCallbacks.containsKey(activity.getClass().getSimpleName())) {
                ComponentCallbacks componentCallbacks = new ComponentCallbacks() {
                    @Override
                    public void onConfigurationChanged(@NonNull Configuration newConfig) {
                        //表示字体发生更改，重新对scaleDensity进行赋值
                        if (newConfig.fontScale > 0) {
                            appScaleDensity = activity.getApplication().getResources().getDisplayMetrics().scaledDensity;
                        }
                    }

                    @Override
                    public void onLowMemory() {

                    }
                };
                activity.getApplication().registerComponentCallbacks(componentCallbacks);
                mComponentCallbacks.put(activity.getClass().getSimpleName(), componentCallbacks);
            }
        }

        //设置字体缩放大小
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换Activity的density,scaleDensity,densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;
    }

    /**
     * 保证在应用进入热启动之前 清除所以density修改 保证热启动闪屏ui 不被 density和系统初始化的density发生拉扯情况
     *
     * @param activity 需要取消的activity
     */
    public static void resetDensity(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.setToDefaults();

        if (mComponentCallbacks == null) {
            return;
        }

        if (mComponentCallbacks.containsKey(activity.getClass().getSimpleName())) {
            ComponentCallbacks componentCallbacks = mComponentCallbacks.get(activity.getClass().getSimpleName());
            activity.getApplication().unregisterComponentCallbacks(componentCallbacks);
            mComponentCallbacks.remove(activity.getClass().getSimpleName());
        }

        if (mComponentCallbacks.isEmpty()) {
            mComponentCallbacks = null;
        }
//        dm.density = appDensity;
//        dm.scaledDensity = appScaleDensity;
//        dm.densityDpi = (int) (appDensity * 160);
    }

}
