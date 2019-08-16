package com.gionee.gnservice.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.utils.PreconditionsUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;

public class ImageLoaderImpl implements IImageLoader {
    private static ImageLoaderImpl INSTANCE;
    private ImageLoader mImageLoader;
    private Context mContext;

    private ImageLoaderImpl(Context context) {
        mContext = context.getApplicationContext();
        mImageLoader = ImageLoader.getInstance();
        initImageLoader(mContext);
    }

    public static IImageLoader create(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ImageLoaderImpl(context);
        }
        return INSTANCE;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(AppConfig.ImageLoader.THREAD_POOL_SIZE);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(AppConfig.ImageLoader.CACHE_IMAGE_DISK_MAX_SIZE);
        config.diskCache(new UnlimitedDiskCache(new File(mContext.getFilesDir(), AppConfig.ImageLoader.CACHE_DIRECTORY_NAME)));
        config.diskCacheFileCount(AppConfig.ImageLoader.CACHE_IMAGE_DISK_MAX_COUNT);
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
        config.writeDebugLogs();
        config.memoryCacheSize(AppConfig.ImageLoader.CACHE_IMAGE_MEMORY_MAX_SIZE);
        config.memoryCache(new UsingFreqLimitedMemoryCache(AppConfig.ImageLoader.CACHE_IMAGE_MEMORY_MAX_SIZE));
        mImageLoader.init(config.build());
    }

    private DisplayImageOptions buildOption(ImageConfig imageConfig) {
        DisplayImageOptions.Builder config = new DisplayImageOptions.Builder();
        if (imageConfig.getPlaceholder() != -1) {
            config.showImageOnLoading(imageConfig.getPlaceholder());

        }
        if (imageConfig.getErrorPic() != -1) {
            config.showImageOnFail(imageConfig.getErrorPic());
        }

        config.cacheInMemory(imageConfig.isCacheInMemory());
        config.cacheOnDisk(imageConfig.isCacheInDisk());
        config.considerExifParams(true);
        config.displayer(new SimpleBitmapDisplayer());
        config.bitmapConfig(Bitmap.Config.RGB_565);
        config.imageScaleType(ImageScaleType.NONE);
        return config.build();
    }

    @Override
    public void loadImage(ImageConfig config) {
        PreconditionsUtil.checkNotNull(config.getUrl());
        PreconditionsUtil.checkNotNull(config.getImageView());
        mImageLoader.displayImage(config.getUrl(), config.getImageView(), buildOption(config), config.getListener());
    }
}
