package code.lee.code.library;

import android.app.Activity;

import code.lee.code.library.listener.RequestPermission;

/**
 * @author jv.lee
 * @date 2019/4/13
 */
public class PermissionManager {
    public static void request(Activity activity, String[] permissions) {
        String className = activity.getClass().getName() + "$Permissions";
        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.requestPermission(activity,permissions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, int[] grantResults) {
        String className = activity.getClass().getName() + "$Permissions";
        try {
            Class<?> clazz = Class.forName(className);
            RequestPermission requestPermission = (RequestPermission) clazz.newInstance();
            requestPermission.onRequestPermissionsResult(activity,requestCode,grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
