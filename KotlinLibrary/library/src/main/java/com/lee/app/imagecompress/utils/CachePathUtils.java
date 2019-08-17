package com.lee.app.imagecompress.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 缓存文件夹 文件名工具
 */
public class CachePathUtils {

    /**
     * 独立创建拍照路径 通过此方法创建android 缓存目录 包名下的文件夹作为缓存存储
     * @param fileName 图片名
     * @return 缓存文件夹路径
     */
    private static File getCameraCacheDir(String fileName) {
        File cache = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!cache.mkdirs() && (!cache.exists() || !cache.isDirectory())) {
            return null;
        }else{
            return new File(cache, fileName);
        }
    }

    /**
     * 获取图片文件名 创建时间戳为文件名
     * @return 图片文件名
     */
    private static String getBaseFileName(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
    }

    /**
     * 获取拍照缓存文件
     * @return 缓存文件
     */
    public static File getCameraCacheFile(){
        String fileName = "camera_" + getBaseFileName() + ".jpg";
        return getCameraCacheDir(fileName);
    }

}
