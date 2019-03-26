package com.lee.code.pixel.test;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PixelUtils {

    private static PixelUtils instance;

    //设计稿的尺寸
    private static final float STANDARD_WIDTH = 720;
    private static final float STANDARD_HEIGHT = 1280;

    //屏幕的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;

    public static PixelUtils getInstance(Context context){
        if (instance == null) {
            synchronized (PixelUtils.class) {
                if (instance == null) {
                    instance = new PixelUtils(context);
                }
            }
        }
        return instance;
    }

    private PixelUtils(Context context){
        if (mDisplayWidth == 0 || mDisplayHeight == 0) {
            WindowManager windos = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windos != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                windos.getDefaultDisplay().getMetrics(displayMetrics);
                if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                    //横屏
                    mDisplayWidth = displayMetrics.heightPixels;
                    mDisplayHeight = displayMetrics.widthPixels;
                }else{
                    //竖屏
                    mDisplayWidth = displayMetrics.widthPixels;
                    mDisplayHeight = displayMetrics.heightPixels - getStatusBarHeight(context);
                }
            }
        }
    }

    private int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }


    public float getHorizontalScale(){
        return mDisplayWidth / STANDARD_WIDTH;
    }

    public float getVerticalScale(){
        return mDisplayHeight / STANDARD_HEIGHT;
    }

}
