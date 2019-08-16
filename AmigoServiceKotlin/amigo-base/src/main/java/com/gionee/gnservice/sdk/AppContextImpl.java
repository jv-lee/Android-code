package com.gionee.gnservice.sdk;

import android.app.Application;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.SharedPrefHelper;
import com.gionee.gnservice.common.account.AccountHelperImpl;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.cache.lru.SimpleLruDiskCache;
import com.gionee.gnservice.common.http.HttpHelperImpls;
import com.gionee.gnservice.common.http.IHttpHelper;
import com.gionee.gnservice.config.AppConfig;

/**
 * Created by caocong on 5/9/17.
 */
class AppContextImpl implements IAppContext {
    private Application mApplication;
    private SharedPrefHelper mSharePrefHelper;
    private ICacheHelper mCacheHelper;
    private IHttpHelper mHttpHelper;
    private IAccountHelper mAccountHelper;

    public AppContextImpl(Application application) {
        mApplication = application;
    }

    @Override
    public Application application() {
        return mApplication;
    }

    @Override
    public IAccountHelper accountHelper() {
        if (mAccountHelper == null) {
            mAccountHelper = new AccountHelperImpl(mApplication.getApplicationContext());
        }
        return this.mAccountHelper;
    }

    @Override
    public SharedPrefHelper sharedPrefHelper() {
        if (mSharePrefHelper == null) {
            mSharePrefHelper = SharedPrefHelper.getInstance(mApplication);
        }
        return this.mSharePrefHelper;
    }

    @Override
    public ICacheHelper cacheHelper() {
        if (mCacheHelper == null) {
            mCacheHelper = new SimpleLruDiskCache(mApplication.getCacheDir(), AppConfig.CacheHelper.CACHE_MAX_SIZE,
                    AppConfig.CacheHelper.CACHE_MAX_COUNT);
        }
        return this.mCacheHelper;
    }

    @Override
    public IHttpHelper httpHelper() {
        if (mHttpHelper == null) {
            mHttpHelper = new HttpHelperImpls(mApplication);
        }
        return this.mHttpHelper;
    }

}
