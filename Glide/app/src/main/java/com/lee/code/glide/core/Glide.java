package com.lee.code.glide.core;

import android.app.Activity;

import com.lee.code.glide.request.BitmapRequest;

public class Glide {

    public static BitmapRequest with(Activity activity) {
        return new BitmapRequest(activity);
    }
}
