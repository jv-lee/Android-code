package com.lee.permission.menu;

import android.content.Context;
import android.content.Intent;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description
 */
public interface IMenu {
    /**
     * 定义跳转设置界面
     * @param context
     * @return
     */
    Intent getStartActivity(Context context);
}
