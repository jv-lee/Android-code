package com.lee.library.imagecompress;

import android.content.Context;
import android.text.TextUtils;

import com.lee.library.imagecompress.bean.Photo;
import com.lee.library.imagecompress.config.CompressConfig;
import com.lee.library.imagecompress.core.CompressImageUtil;
import com.lee.library.imagecompress.listener.CompressImage;
import com.lee.library.imagecompress.listener.CompressResultListener;

import java.io.File;
import java.util.ArrayList;

/**
 * 框架：思路，千万不要过度的封装
 * 1、纠结一个单列的问题
 * 2、能否重复压缩
 * @author jv.lee
 */
public class CompressImageManager implements CompressImage {

    /**
     * 压缩工具类
     */
    private CompressImageUtil compressImageUtil;
    /**
     * 需要压缩的图片集合
     */
    private ArrayList<Photo> images;
    /**
     * 压缩监听 告知调用 Activity
     */
    private CompressImage.CompressListener listener;
    /**
     * 压缩配置
     */
    private CompressConfig config;

    private CompressImageManager(Context context, CompressConfig config, ArrayList<Photo> images, CompressListener listener) {
        compressImageUtil = new CompressImageUtil(context, config);
        this.config = config;
        this.images = images;
        this.listener = listener;
    }

    public static CompressImage build(Context context, CompressConfig config, ArrayList<Photo> images, CompressListener listener) {
        return new CompressImageManager(context, config, images, listener);
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, "图片集合为空");
            return;
        }

        for (Photo image : images) {
            if (image == null) {
                listener.onCompressFailed(images, "压缩时发现某图片对象为空");
                return;
            }
        }

        //开始递归 ，从第一张图片对象开始压缩
        compress(images.get(0));
    }

    /**
     * 从第一张开始, index = 0
     * @param image
     */
    private void compress(final Photo image) {
        if (TextUtils.isEmpty(image.getOriginalPath())) {
            continueCompress(image, false);
            return;
        }

        File file = new File(image.getOriginalPath());
        if (!file.exists() || !file.isFile()) {
            continueCompress(image, false);
            return;
        }

        // <= 200KB
        if (file.length() < config.getMaxSize()) {
            continueCompress(image, true);
            return;
        }

        //条件满足 开始压缩
        compressImageUtil.compress(image.getOriginalPath(), new CompressResultListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                //压缩成功
                image.setCompressPath(imgPath);
                continueCompress(image, true);
            }


            @Override
            public void onCompressFailed(String imgPath, String error) {
                //压缩失败
                continueCompress(image, false, error);
            }
        });
    }

    /**
     * 递归压缩，比较index是否最后一张
     * @param image
     * @param b
     * @param error
     */
    private void continueCompress(Photo image, boolean b, String... error) {
        //给图片对象设置是否成功属性
        image.setCompressed(b);
        //当前的图片的索引
        int index = images.indexOf(image);
        //判断是否最后一张
        if (index == images.size() - 1) {
            handlerCallback(error);
        }else{
            //下一个图片对象 开始递归
            compress(images.get(index + 1));
        }
    }

    private void handlerCallback(String... error) {
        //如果存在错误信息
        if (error.length > 0) {
            listener.onCompressFailed(images,"某一张图片压缩失败了...");
            return;
        }

        for (Photo photo : images) {
            //如果存在没有压缩的图片，或者压缩失败的
            if (!photo.isCompressed()) {
                listener.onCompressFailed(images,"某一张图片压缩失败了...");
                return;
            }
        }

        listener.onCompressSuccess(images);
    }

}
