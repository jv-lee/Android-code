package com.gionee.gnservice.base;

import android.app.Application;

import com.gionee.gnservice.common.SharedPrefHelper;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.common.cache.ICacheHelper;
import com.gionee.gnservice.common.http.IHttpHelper;

public interface IAppContext {

    Application application();

    IAccountHelper accountHelper();

    SharedPrefHelper sharedPrefHelper();

    ICacheHelper cacheHelper();

    IHttpHelper httpHelper();
}
