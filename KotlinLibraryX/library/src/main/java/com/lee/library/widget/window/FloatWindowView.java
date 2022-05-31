package com.lee.library.widget.window;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 悬浮窗view
 * @author jv.lee
 * @date 2019/9/5.
 */
public class FloatWindowView implements Application.ActivityLifecycleCallbacks {

    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams wmParams;
    private View view;

    private float x;
    private float y;
    private float mTouchX;
    private float mTouchY;
    private int startX;
    private int startY;
    private int screenWidth;
    private int screenHeight;
    private int space = 20;

    private boolean isAdd;
    private boolean isRebound;
    private List<Class> classList = new ArrayList<>();

    private View.OnClickListener onClickListener;

    public FloatWindowView(Context context) {
        this.context = context;
        ((Application) context).registerActivityLifecycleCallbacks(this);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public void bindView(View view) {
        this.view = view;
        createFloatWindow();
    }

    public void bindLayout(int layoutId) {
        view = LayoutInflater.from(context).inflate(layoutId, null);
        createFloatWindow();
    }

    private void createFloatWindow() {
        wmParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.x = (int) (screenWidth * 0.9);
        wmParams.y = (int) (screenHeight * 0.7);
        //设置window宽高
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        setTouchMove(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchMove(View view) {
        view.setOnTouchListener((v, event) -> {
            x = event.getRawX();
            y = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchX = event.getX();
                    mTouchY = event.getY();
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    movePosition(view);
                    break;
                case MotionEvent.ACTION_UP:
                    //计算偏移量 是否达到点击条件
                    if (Math.abs(x - startX) < space && Math.abs(y - startY) < space) {
                        if (onClickListener != null) {
                            onClickListener.onClick(v);
                        }
                    }
                    //是否开启回弹
                    if (isRebound) {
                        if (x <= screenWidth / 2) {
                            x = 0;
                        } else {
                            x = screenWidth;
                        }
                        movePosition(view);
                    }
                    break;
                default:
            }

            return false;
        });
    }

    private void movePosition(View view) {
        wmParams.x = (int) (x - mTouchX);
        wmParams.y = (int) (y - mTouchY);
        windowManager.updateViewLayout(view, wmParams);
    }

    public void showWindow() {
        if (!isAdd) {
            isAdd = true;
            windowManager.addView(view, wmParams);
        }
    }

    public void hideWindow() {
        if (isAdd) {
            isAdd = false;
            windowManager.removeView(view);
        }
    }

    public void bindActivity(Class[] classes) {
        classList.addAll(Arrays.asList(classes));
    }

    /**
     * 是否开启回弹
     *
     * @param rebound
     */
    public void setRebound(boolean rebound) {
        isRebound = rebound;
    }

    /**
     * 设置点击监听
     *
     * @param onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        for (Class act : classList) {
            if (act.getName().contains(activity.getLocalClassName())) {
                showWindow();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (Class act : classList) {
            if (act.getName().contains(activity.getLocalClassName())) {
                hideWindow();
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void unBind() {
        ((Application) context).unregisterActivityLifecycleCallbacks(this);
    }

}
