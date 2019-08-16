package com.gionee.gnservice.module.main.banner;

import android.content.Context;
import android.widget.ImageView;

import com.gionee.gnservice.common.imageloader.IImageLoader;
import com.gionee.gnservice.common.imageloader.ImageConfig;
import com.gionee.gnservice.common.imageloader.ImageLoaderImpl;


public class BannerImageLoader implements IBannerImageLoader<ImageView> {
    private IImageLoader imageLoader;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (imageLoader == null) {
            imageLoader = ImageLoaderImpl.create(context);
        }
        ImageConfig.Builder builder = new ImageConfig.Builder();
        builder.setUrl((String) path)
                .setImageView(imageView)
                .setCacheInDisk(true)
                .setCacheInMemory(true);
        imageLoader.loadImage(builder.build());
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}
