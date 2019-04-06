package com.lee.library.imagecompress.listener;

/**
 * 单张图片压缩监听
 * @author jv.lee
 */
public interface CompressResultListener {
    /**
     * 成功
     * @param imgPath
     */
    void onCompressSuccess(String imgPath);

    /**
     * 失败
     * @param imgPath
     * @param error
     */
    void onCompressFailed(String imgPath,String error);
}
