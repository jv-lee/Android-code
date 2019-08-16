package com.gionee.gnservice.module.setting.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public final class PushHelper {

    private PushHelper() throws IllegalAccessException {
        throw new IllegalAccessException("");
    }

    public static void registerPushRid(Context context) {
        // 第一步,取出自己保存的RID
        String rid = readRid(context);
        // 第二步,检查自己的RID是否正确
        if ("-1".equals(rid)) {
            // 2.1说明还没有注册或者没注销掉了,那么直接注册即可
            registerRid(context);
        } else {
            // 2.2如果有RID,没有问题,可以根据自己应用的情况做些事情,例如检查是否已经注册自己应用的APS
            ReceiverNotifier.getInstance().notifyRidGot(rid);
        }
    }

    public static void registerRid(Context context) {
        Intent intent = new Intent().setAction("com.gionee.cloud.intent.REGISTER").putExtra("packagename",
                context.getPackageName());
        intent.setPackage("com.gionee.cloud.gpe");
        context.startService(intent);
    }

    public static String readRid(Context mContext) {
        String rid = null;
        SharedPreferences mPreference = mContext.getSharedPreferences("gn_service_push", Context.MODE_PRIVATE);
        rid = mPreference.getString("rid", "-1");
        Log.d("SERVICE_PUSH", "read rid = " + rid);
        return rid;
    }

    public static void writeRid(Context mContext, String value) {
        Log.d("SERVICE_PUSH", "write rid = " + value);
        SharedPreferences mPreference = mContext.getSharedPreferences("gn_service_push", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreference.edit();
        mEditor.putString("rid", value);
        mEditor.apply();

    }

    public static void unregisterRid(Context context) {
        Intent intent = new Intent().setAction("com.gionee.cloud.intent.UNREGISTER").putExtra("packagename",
                context.getPackageName());
        intent.setPackage("com.gionee.cloud.gpe");
        context.startService(intent);
    }
}
