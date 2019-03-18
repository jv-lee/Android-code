package com.lee.code.glide.listener;

import android.graphics.Bitmap;

public interface RequestListener {
    boolean onException();

    boolean onResourceReady(Bitmap resource);
}
