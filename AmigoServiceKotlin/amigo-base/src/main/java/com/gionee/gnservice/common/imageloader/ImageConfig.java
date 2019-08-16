package com.gionee.gnservice.common.imageloader;

import android.widget.ImageView;

import com.gionee.gnservice.utils.PreconditionsUtil;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageConfig {
    private String url;
    private ImageView imageView;
    private int placeholder = -1;
    private int errorPic = -1;
    private boolean cacheInMemory = true;
    private boolean cacheInDisk = true;
    private ImageLoadingListener listener;

    private ImageConfig(Builder builder) {
        this.url = PreconditionsUtil.checkNotNull(builder.url);
        this.imageView = PreconditionsUtil.checkNotNull(builder.imageView);
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.cacheInDisk = builder.cacheInDisk;
        this.cacheInMemory = builder.cacheInMemory;
        this.listener = builder.listener;
    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

    public boolean isCacheInMemory() {
        return cacheInMemory;
    }

    public boolean isCacheInDisk() {
        return cacheInDisk;
    }

    public ImageLoadingListener getListener() {
        return listener;
    }

    public static final class Builder {
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private boolean cacheInMemory = true;
        private boolean cacheInDisk = true;
        private ImageLoadingListener listener;

        public Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder setPlaceholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder setErrorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Builder setCacheInMemory(boolean cacheInMemory) {
            this.cacheInMemory = cacheInMemory;
            return this;
        }

        public Builder setCacheInDisk(boolean cacheInDisk) {
            this.cacheInDisk = cacheInDisk;
            return this;
        }

        public Builder setListener(ImageLoadingListener listener) {
            this.listener = listener;
            return this;
        }

        public ImageConfig build() {
            PreconditionsUtil.checkNotNull(url);
            PreconditionsUtil.checkNotNull(imageView);
            return new ImageConfig(this);
        }
    }
}
