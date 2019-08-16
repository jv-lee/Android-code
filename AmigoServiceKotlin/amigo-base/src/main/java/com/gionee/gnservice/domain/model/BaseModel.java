package com.gionee.gnservice.domain.model;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.utils.NetworkUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

public abstract class BaseModel {
    protected IAppContext mAppContext;

    public BaseModel(IAppContext appContext) {
        PreconditionsUtil.checkNotNull(appContext);
        mAppContext = appContext;
    }

    protected boolean isNetworkConnect() {
        return NetworkUtil.isConnected(mAppContext.application());
    }
}
