package com.lee.library.tool;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏工具
 *
 * @author jv.lee
 * @date 2019/4/5
 */
public class StatusTool {

    /**
     * 设置沉浸式状态栏
     *
     * @param activity                 引用
     * @param navigationBarTranslucent 导航栏是否设置为透明
     */
    public static void statusBar(Activity activity, boolean navigationBarTranslucent) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //5.0以设置沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色调整
            window.setStatusBarColor(Color.TRANSPARENT);

            int visibility = window.getDecorView().getSystemUiVisibility();
            //布局内容全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //隐藏虚拟导航栏
//            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            //防止内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(visibility);
            //4.0设置
        } else {
            //设置沉浸式 状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置沉浸式 导航栏
            if (navigationBarTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 设置状态栏颜色 黑色
     *
     * @param activity
     */
    public static void setStatusFontLight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取窗口区域
            Window window = activity.getWindow();
            //设置显示为黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 保持原有flag 设置深色状态栏颜色
     *
     * @param activity
     */
    public static void setStatusFontLight2(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(originFlag | View
                    .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 保留原有flag 清除深色状态栏颜色
     *
     * @param activity
     */
    public static void claerStatusFontLight2(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
            //使用异或清除SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.getWindow().getDecorView().setSystemUiVisibility(originFlag ^ View
                    .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 全屏模式
     *
     * @param activity
     */
    public static void fullWindow(Activity activity) {
        //1.设置全屏幕
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 设置自定义toolbar高度
     *
     * @param context
     * @param view
     */
    public static void setStatusPadding(Context context, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int statusHeight = getStatusBarHeight(context);
        layoutParams.height += statusHeight;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusHeight, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setStatusPadding(Context context, Toolbar view) {
        int statusHeight = getStatusBarHeight(context);
        view.setPadding(0, statusHeight, 0, 0);
    }

}
