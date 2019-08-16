package com.gionee.gnservice.module.integral;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;

import com.gionee.gnservice.utils.LogUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * Created by zhongyx on 9/19/17.
 */

class ImageTintListener implements ImageLoadingListener {
    private static final String TAG = "ImageTintListener";

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        LogUtil.i(TAG, "onLoadingComplete() (view == null) ? " + (view == null));
        if (view instanceof ImageView) {
            LogUtil.d(TAG, "setIconGrey() view is an instance of ImageView ");
            tintImage2Gray((ImageView) view);
        }
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }

    private void tintImage2Gray(ImageView imageView) {
        Drawable originDrawable = imageView.getDrawable();
        if (originDrawable == null) {
            return;
        }
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable.mutate());
        DrawableCompat.setTintList(tintDrawable, ColorStateList.valueOf(0x66828282));
        tintDrawable.setBounds(0, 0, tintDrawable.getIntrinsicWidth(),
                tintDrawable.getIntrinsicHeight());
        imageView.setImageDrawable(tintDrawable);
    }
}
