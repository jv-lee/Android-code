package com.gionee.gnservice.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.utils.PhoneUtil;

public class BaseModule implements IModule {
    protected Context mContext;
    private ServiceInfo mModuleInfo;

    BaseModule(Context context) {
        this.mContext = context;
    }

    public BaseModule(Context context, ServiceInfo moduleInfo) {
        this.mContext = context;
        this.mModuleInfo = moduleInfo;
    }

    private void startApkActivity() {
        Intent intent;
        if (moduleInfo().isAction()) {
            intent = new Intent(moduleInfo().getTarget());
            if (!TextUtils.isEmpty(moduleInfo().getTargetPackage())) {
                intent.setPackage(moduleInfo().getTargetPackage());
            }
        } else {
            intent = new Intent();
            intent.setClassName(moduleInfo().getTargetPackage(), moduleInfo().getTarget());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }

    private void startLocalActivity() {
        Intent intent = new Intent();
        intent.setClassName(mContext, moduleInfo().getTarget());
        mContext.startActivity(intent);
    }

    private boolean isAppExist(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    private boolean isAppExistByAction(String Action) {
        ResolveInfo ri = mContext.getPackageManager().resolveActivity(new Intent(Action),
                PackageManager.MATCH_DEFAULT_ONLY);
        return ri != null;
    }

    @Override
    public void startModule() {
        try {
            if (!moduleInfo().isApk()) {
                startLocalActivity();
                return;
            }

            if (isModuleExist()) {
                startApkActivity();
            } else {
                PhoneUtil.recoverRemovableApp(mContext.getApplicationContext(), moduleInfo().getTargetPackage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceInfo moduleInfo() {
        if (mModuleInfo == null) {
            throw new NullPointerException("moduleInfo can not be null");
        }
        return this.mModuleInfo;
    }

    @Override
    public boolean isModuleExist() {
        if (moduleInfo().isAction()) {
            return isAppExistByAction(moduleInfo().getTarget());
        }
        return isAppExist(moduleInfo().getTargetPackage());
    }
}
