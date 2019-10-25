package com.lee.permission;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.lee.permission.core.IPermission;

import java.lang.ref.WeakReference;

/**
 * @author jv.lee
 * @date 2019/4/14
 */
public class PermissionManager {

    private static PermissionManager instance;

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }

    private WeakReference<Activity> weakActivity;
    private String[] permissions;
    public static final String PERMISSION_TAG = "permission";
    private static final int PERMISSION_CODE = 1001;

    /**
     * 默认自动跳转设置
     */
    private boolean startSettings = true;

    public PermissionManager attach(Activity activity) {
        weakActivity = new WeakReference<>(activity);
        return this;
    }

    public PermissionManager request(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void listener(IPermission iPermission) {
        PermissionFragment fragment = PermissionFragment.getPermissionFragment(permissions, PERMISSION_CODE, iPermission);
        FragmentManager manager = weakActivity.get().getFragmentManager();
        bindFragment(manager, fragment);
    }

    public void bindFragment(FragmentManager manager, PermissionFragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.remove(fragment);
        }
        transaction.add(fragment, PERMISSION_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }

    boolean isStartSettings() {
        return startSettings;
    }

    public void setSettings(boolean enable) {
        this.startSettings = enable;
    }
}
