package com.lee.code.pixel;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

public class DisplayCutoutActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置全屏幕
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //华为、小米、oppo  1.判断手机厂商 2.判断手机是否有刘海 3.设置是否让内容区域延伸进刘海  4.设置控件避开刘海区域

        //2.判断手机是否是刘海屏
         boolean hasDisplayCutout = hasDisplayCutout(window);
        if (hasDisplayCutout) {//有刘海 设置沉浸式


            //3.设置沉浸式 内容延伸进statusBar
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flags; //追加沉浸式设置
            window.getDecorView().setSystemUiVisibility(visibility);
        }

        setContentView(R.layout.activity_display_cutout);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    /**
     * 判断手机是不是刘海屏
     */
    private boolean hasDisplayCutout(Window window) {
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = rootView.getRootWindowInsets();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets != null) {
            displayCutout = insets.getDisplayCutout();
            if (displayCutout != null) {//判断是否有刘海
                if (displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0 && displayCutout.getSafeInsetTop() > 0) {//判断有几个刘海
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取刘海屏幕的高度  通常情况刘海的高度就相当于状态栏的高度
     * @return
     */
    public int heightForDisplayCutout(){
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0) {
            return getResources().getDimensionPixelSize(resID);
        }
        return 0;
    }
}
