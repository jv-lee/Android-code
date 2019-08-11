package com.lee.plugin;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * @author jv.lee
 * @date 2019-08-10
 * @description
 */
public class BaseActivity extends Activity {

    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null) {
            return getApplication().getResources();
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (getApplication() != null && getApplication().getAssets() != null) {
            return getApplication().getAssets();
        }
        return super.getAssets();
    }
}
