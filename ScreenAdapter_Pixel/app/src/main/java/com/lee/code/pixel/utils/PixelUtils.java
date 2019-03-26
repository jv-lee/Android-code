package com.lee.code.pixel.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PixelUtils {

    private static PixelUtils instance;

    //设计稿参考的宽高
    private static final float STANDARD_WIDTH = 720;
    private static final float STANDARD_HEIGHT = 1280;

    //屏幕显示的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;

    private PixelUtils(Context context){
        //获取屏幕宽高
        if (mDisplayWidth == 0 || mDisplayHeight == 0) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                    //横屏
                    mDisplayWidth = displayMetrics.heightPixels;
                    mDisplayHeight = displayMetrics.widthPixels;
                }else{
                    mDisplayWidth = displayMetrics.widthPixels;
                    mDisplayHeight = displayMetrics.heightPixels - getStatusBarHeight(context);
                }
            }
        }
    }

    public static PixelUtils getInstance(Context context){
        if (instance == null) {
            synchronized (PixelUtils.class) {
                if (instance == null) {
                    instance = new PixelUtils(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    //获取状态栏高度
    private int getStatusBarHeight(Context context){
        int resID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0) {
            return context.getResources().getDimensionPixelSize(resID);
        }
        return 0;
    }

    /**
     * 获取水平方向的缩放比例
     * @return
     */
    public float getHorizontalScale(){
        return mDisplayWidth / STANDARD_WIDTH;
    }

    /**
     * 获取垂直方向的缩放比例
     * @return
     */
    public float getVerticalScale(){
        return mDisplayHeight / STANDARD_HEIGHT;
    }

}
