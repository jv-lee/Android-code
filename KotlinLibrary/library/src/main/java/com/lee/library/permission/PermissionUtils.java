package com.lee.library.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限申请工具类
 * @author jv.lee
 * @date 2019/4/13
 */
public class PermissionUtils {
    private PermissionUtils(){}

    /**
     * 检查所有权限是否已允许
     * @param grantResults 授权结果
     * @return 如果所有权限已允许，则返回true
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限组
     * @param context 上下文
     * @param permissions 权限集合
     * @return 如果所有权限已允许则返回true
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        //如果低于6.0无须做运行时权限判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (!hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户申请权限
     * @param context 上下文
     * @param permission 权限
     * @return 是否有权限
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查被拒绝的权限组中，是否有点击了"不再询问"的权限
     * 第一次打开app时 false
     * 上次弹出权限请求点击了拒绝，但没有勾选"不在讯问" true
     * 上次弹出权限请求时点击了拒绝 并且勾选了"不在讯问" false
     * 点击了拒绝，但没有勾选"不再询问"返回true，点击了拒绝，并且勾选了"不在讯问"返回false
     * @param activity 目前对象
     * @param permissions 被拒绝的权限组
     * @return 如果有任意一个"不再询问"的权限 返回true，否则返回false
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

}
