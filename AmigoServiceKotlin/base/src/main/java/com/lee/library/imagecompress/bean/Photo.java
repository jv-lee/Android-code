package com.lee.library.imagecompress.bean;

import java.io.Serializable;

/**
 * @author jv.lee
 */
public class Photo implements Serializable {
    /**
     * 图片原始路径
     */
    private String originalPath;
    /**
     * 图片是否压缩过
     */
    private boolean compressed;
    /**
     * 图片压缩后路径
     */
    private String compressPath;


    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public Photo(String originalPath) {
        this.originalPath = originalPath;
    }
}
