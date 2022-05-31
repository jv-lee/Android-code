package com.lee.library.widget.window;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.lee.library.intent.IntentManager;


/**
 *
 * @author jv.lee
 * @date 2019/9/9.
 *
 */
public class FloatWindowManager {

    public void checkPermission(Activity activity, WindowCallback windowCallback) {
        // 7.0 以上需要引导用去设置开启窗口浮动权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestDrawOverLays(activity, windowCallback);
            // 6.0 动态申请
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED){
                windowCallback.filed();
            }else{
                windowCallback.success();
            }
        }
    }

    /**
     * android 23 以上先引导用户开启这个权限 该权限动态申请不了
     *
     * @param activity
     * @param windowCallback
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestDrawOverLays(Activity activity, WindowCallback windowCallback) {
        if (!Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            IntentManager.getInstance()
                    .startActForResult((FragmentActivity) activity, intent, (requestCode, resultCode, data) -> {
                        if (!Settings.canDrawOverlays(activity)) {
                            windowCallback.filed();
                        } else {
                            windowCallback.success();
                        }
                    });
        } else {
            windowCallback.success();
        }
    }

}
