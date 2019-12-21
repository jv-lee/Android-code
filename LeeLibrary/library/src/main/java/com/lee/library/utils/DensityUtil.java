package com.lee.library.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * @author jv.lee
 */
public class DensityUtil {

    /**
     * 参考设备的宽，单位：dp
     */
    private static final float WIDTH = 375;

    /**
     * 表示屏幕密度
     */
    private static float appDensity;
    /**
     * 字体的缩放比例，默认appDensity
     */
    private static float appScaleDensity;

    /**
     * 修改当前activity的缩放比例 调整dpi值
     * @param application
     * @param activity
     */
    public static void setDensity(final Application application, Activity activity) {
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0) {
            //初始化赋值操作
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //表示字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //计算目标值 density,scaledDensity,densityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH;
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
     * @param activity
     */
    public static void resetDensity(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = appDensity;
        dm.scaledDensity = appScaleDensity;
        dm.densityDpi = (int)(appDensity * 160);
    }

}
