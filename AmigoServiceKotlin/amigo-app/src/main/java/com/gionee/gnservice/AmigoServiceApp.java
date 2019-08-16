package com.gionee.gnservice;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import com.gionee.gnservice.base.BaseApplication;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.SharedPrefHelper;
import com.gionee.gnservice.common.account.AccountHelperImpl;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.cache.lru.SimpleLruDiskCache;
import com.gionee.gnservice.common.http.HttpHelperImpls;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.module.setting.push.PushHelper;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.statistics.StatisticsUtil;

public class AmigoServiceApp extends BaseApplication implements IAppContext {
    private static final String TAG = AmigoServiceApp.class.getSimpleName();
    private static final boolean STRICT_MODE = false;

    private SharedPrefHelper mSharePrefHelper;

    private ICacheHelper mCacheHelper;

    private volatile IHttpHelper mHttpHelper;

    private IAccountHelper mAccountHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        initStrictMode();
        initVariables();
        initPushHelper();
        StatisticsUtil.init(this);
        AmigoServiceSdk.getInstance().init(this);
    }

    /**
     * 获取当前进程名
     */
    public String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    protected void initVariables() {
        mAccountHelper = new AccountHelperImpl(this);
    }

    @Override
    public void exit() {
        mSharePrefHelper = null;
        mCacheHelper = null;
        mHttpHelper = null;
        mAccountHelper = null;
        super.exit();
    }

    private void initPushHelper() {
        PushHelper.registerPushRid(this);
    }

    private void initStrictMode() {
        if (STRICT_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDialog()
                    .penaltyFlashScreen()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }
    }

    @Override
    public Application application() {
        return this;
    }

    @Override
    public IAccountHelper accountHelper() {
        return this.mAccountHelper;
    }

    @Override
    public SharedPrefHelper sharedPrefHelper() {
        if (mSharePrefHelper == null) {
            synchronized (this) {
                if (mSharePrefHelper == null) {
                    mSharePrefHelper = SharedPrefHelper.getInstance(this);
                }
            }
        }
        return this.mSharePrefHelper;
    }

    @Override
    public ICacheHelper cacheHelper() {
        if (mCacheHelper == null) {
            synchronized (this) {
                if (mCacheHelper == null) {
                    mCacheHelper = new SimpleLruDiskCache(getCacheDir(), AppConfig.CacheHelper.CACHE_MAX_SIZE,
                            AppConfig.CacheHelper.CACHE_MAX_COUNT);
                }
            }
        }
        return this.mCacheHelper;
    }

    @Override
    public IHttpHelper httpHelper() {
        if (mHttpHelper == null) {
            synchronized (this) {
                if (mHttpHelper == null) {
                    mHttpHelper = new HttpHelperImpls(this);
                }
            }
        }
        return this.mHttpHelper;
    }

}
