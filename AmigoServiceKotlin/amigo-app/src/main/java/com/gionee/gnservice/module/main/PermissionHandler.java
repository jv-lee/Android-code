package com.gionee.gnservice.module.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import com.gionee.gnservice.R;
import com.gionee.gnservice.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caocong on 4/20/17.
 */

public class PermissionHandler {
    private static final String TAG = PermissionHandler.class.getSimpleName();
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_LENGTH = PERMISSIONS.length;
    private WeakReference<Activity> mActivity;

    public PermissionHandler(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    @TargetApi(23)
    public void reqeustPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        Activity activity = mActivity.get();
        if (activity == null) {
            return;
        }
        List<String> permissionList = new ArrayList<String>();
        for (int i = 0; i < PERMISSIONS_LENGTH; i++) {
            String permission = PERMISSIONS[i];
            if (activity.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                LogUtil.d(TAG, "user has the permission" + permission + " already!");
            } else {
                LogUtil.d(TAG, "user need quest Permissions!" + permission);
                permissionList.add(permission);
            }
        }
        if (permissionList.size() > 0) {
            activity.requestPermissions(permissionList.toArray(new String[permissionList.size()]),
                    PERMISSIONS_LENGTH);
        }

    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Activity activity = mActivity.get();
        if (activity == null) {
            return;
        }
        LogUtil.d(TAG, "onRequestPermissionsResult!");
        if (requestCode == PERMISSIONS_LENGTH) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.READ_PHONE_STATE,
                    PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++) {
                LogUtil.d(TAG, permissions[i] + "is" + grantResults[i]);
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || perms.get(android.Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    activity.finish();
                    Toast.makeText(activity,
                            R.string.uc_user_center_pemission_quest_deny_toast,
                            Toast.LENGTH_LONG).show();
                }
            }

        } else {
            activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
