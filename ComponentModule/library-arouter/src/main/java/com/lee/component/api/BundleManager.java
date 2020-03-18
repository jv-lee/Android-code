package com.lee.component.api;

import android.content.Context;
import android.os.Bundle;

/**
 * @author jv.lee
 * @date 2019-07-22
 * @description 意图参数管理类
 */
public class BundleManager {

    private Bundle bundle = new Bundle();

    /**
     * 是否需要回调的Result
     */
    private boolean isResult;


    public Bundle getBundle() {
        return bundle;
    }

    public boolean isResult() {
        return isResult;
    }

    public BundleManager withString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleManager withResultString(String key, String value) {
        bundle.putString(key, value);
        isResult = true;
        return this;
    }

    public BundleManager withBundle(String key, Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    /**
     * 直接跳转startActivity
     *
     * @param context
     * @return
     */
    public Object navigation(Context context) {
        return navigation(context, -1);
    }

    /**
     * forResult，这里的code，可能是resultCode，也可以是requestCode，取决于isResult
     *
     * @param context
     * @param code    请求码
     * @return
     */
    public Object navigation(Context context, int code) {
        return RouterManager.getInstance().navigation(context, this,code);
    }

}
