package com.lee.library.imagecompress.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.lee.library.imagecompress.config.CompressConfig;
import com.lee.library.imagecompress.listener.CompressResultListener;

/**
 * 压缩图片
 */
public class CompressImageUtil {
    private CompressConfig config;
    private Context context;
    private Handler mHandler = new Handler();

    public CompressImageUtil(Context context, CompressConfig config) {
        this.context = context;
        this.config = config == null ? CompressConfig.getDefaultConfig() : config;
    }

    public void compress(String imgPath, CompressResultListener listener) {
        if (config.isEnablePixelCompress()) {
            try {
                compressImageByPixel(imgPath, listener);
            } catch (Exception e) {
                listener.onCompressFailed(imgPath,String.format("图片压缩失败，%s",e.toString()));
                e.printStackTrace();
            }
        }else{
            compressImageByQuality(BitmapFactory.decodeFile(imgPath), imgPath, listener);
        }
    }
}
