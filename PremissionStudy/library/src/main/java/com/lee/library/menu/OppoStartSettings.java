package com.lee.library.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description
 */
public class OppoStartSettings implements IMenu {
    @Override
    public Intent getStartActivity(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }
}
