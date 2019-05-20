package code.lee.code.permission.permission;


import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.ref.WeakReference;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public class PermissionManager {

    private static PermissionManager instance;
    private PermissionManager(){}
    public static PermissionManager getInstance(){
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }

    private PermissionFragment fragment;
    private WeakReference<FragmentActivity> weakActivity;
    private String[] permissions;
    private static final String PERMISSION_TAG = "permission";
    PermissionRequest permissionRequest;
    static final int PERMISSION_CODE = 1001;

    public PermissionManager attach(FragmentActivity activity){
        weakActivity = new WeakReference<>(activity);
        return this;
    }

    public PermissionManager request(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void listener(PermissionRequest permissionRequest){
        this.permissionRequest = permissionRequest;

        FragmentManager manager = weakActivity.get().getSupportFragmentManager();
        if (fragment == null) {
            fragment = new PermissionFragment();
        }
        manager.beginTransaction()
                .add(fragment, PERMISSION_TAG)
                .commitAllowingStateLoss();
        manager.executePendingTransactions();

        //判断权限
        if (PermissionUtils.hasSelfPermissions(weakActivity.get(), permissions)) {
            permissionRequest.onSuccess();
        }else{
            fragment.requestPermissions(permissions,PermissionManager.PERMISSION_CODE);
        }

//        if (PermissionUtils.hasSelfPermissions(weakActivity.get(), permissions)) {
//            permissionRequest.onSuccess();
//        } else if (PermissionUtils.shouldShowRequestPermissionRationale(weakActivity.get(), permissions)) {
//
//        }else{
//            fragment.requestPermissions(permissions,PermissionManager.PERMISSION_CODE);
//        }

//        for (String permission : permissions) {
//            if (ActivityCompat.checkSelfPermission(weakActivity.get(), permission) == PackageManager.PERMISSION_DENIED) {
//                fragment.requestPermissions(permissions,PermissionManager.PERMISSION_CODE);
//                return;
//            }
//        }
//        permissionRequest.onSuccess();
    }
}
