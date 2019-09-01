package com.lee.code.glide.core;




import android.app.Activity;
import android.app.FragmentManager;

import com.lee.code.glide.lifecycle.RequestManagerFragment;
import com.lee.code.glide.request.BitmapRequest;

public class Glide {

    public static BitmapRequest with(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag("com.jv.code.glide");
        if (current == null) {
            current = new RequestManagerFragment();
            fm.beginTransaction().add(current, "com.jv.code.glide").commitAllowingStateLoss();
        }
        return new BitmapRequest(activity);
    }
}
