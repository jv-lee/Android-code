package com.gionee.gnservice.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public final class MiscUtil {

    public static boolean isAppOnForeground(Context context) {

        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
            if (tasks == null) {
                return false;
            }
            for (ActivityManager.RunningAppProcessInfo processInfo : tasks) {
                return processInfo.processName.equals(context.getPackageName())
                        && (processInfo.importance <= 150);
            }
        } catch (Exception e) {
            LogUtil.e("MiscUtil", "isAppOnForeground e =" + e);
        }
        return false;
    }
}
