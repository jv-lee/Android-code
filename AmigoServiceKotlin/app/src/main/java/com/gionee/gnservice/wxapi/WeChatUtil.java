package com.gionee.gnservice.wxapi;

import android.content.Context;
import gionee.gnservice.app.App;

public class WeChatUtil {
    /**
     * 微信登录
     *
     * @param context
     */
    public static void wxLogin(final Context context, WXLoginListener wxLoginListener) {
        WXEntryActivity.loginWeixin(context, App.wxApi, wxLoginListener);
    }


}
