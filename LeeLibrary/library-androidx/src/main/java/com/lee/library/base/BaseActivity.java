package com.lee.library.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.lee.library.ioc.InjectManager;
import com.lee.library.tool.StatusTool;

/**
 * @author jv.lee
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected FragmentActivity mActivity;
    protected FragmentManager mFragmentManager;
    private long firstTime = 0;
    private boolean hasBackExit = false;
    private boolean fullScreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //默认设置沉浸式
        StatusTool.statusBar(this, false);
        super.onCreate(savedInstanceState);
        mActivity = this;
        mFragmentManager = getSupportFragmentManager();

        //帮助所有子类进行：布局/控件/事件的注入
        InjectManager.inject(this);
        if (fullScreen) {
            //全屏模式
            StatusTool.fullWindow(this);
        }
        bindData(savedInstanceState);
        bindView();
    }

    /**
     * 加载数据
     *
     * @param savedInstanceState 重置回调参数
     */
    protected abstract void bindData(Bundle savedInstanceState);

    /**
     * 设置view基础配置
     */
    protected abstract void bindView();

    /**
     * 是否为全屏模式
     *
     * @return boolean
     */
    protected abstract boolean isFullscreen();

    protected int hasBackExitTimer = 2000;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (hasBackExit) {
                    long secondTime = System.currentTimeMillis();
                    //如果两次按键时间间隔大于2秒，则不退出
                    if (secondTime - firstTime > hasBackExitTimer) {
                        Toast.makeText(this, "再次按下退出", Toast.LENGTH_SHORT).show();
                        //更新firstTime
                        firstTime = secondTime;
                        return true;
                    } else {//两次按键小于2秒时，退出应用
                        finish();
                    }
                }
            default:
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 设置back键位 连按两次才可退出activity
     *
     * @param enable 设置back双击开关
     */
    protected void backExitEnable(boolean enable) {
        hasBackExit = enable;
    }

}
