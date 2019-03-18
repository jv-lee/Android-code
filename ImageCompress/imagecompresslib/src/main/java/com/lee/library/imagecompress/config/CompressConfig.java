package com.lee.library.imagecompress.config;

public class CompressConfig {
    /**
     * 最小像素不压缩
     */
    private int unCompressMinPixel = 1000;
    /**
     * 标准像素不压缩
     */
    private int unCompressNormalPixel = 2000;
    /**
     * 长或款不超过最大像素，单位px
     */
    private int maxPixel = 1200;
    /**
     * 压缩到最大大小 ，单位B
     */
    private int maxSize = 200*1024; //200kb不压缩
    /**
     * 是否启用像素压缩
     */
    private boolean enablePixelCompress = true;
    /**
     * 是否启用质量压缩
     */
    private boolean enableQualityCompress = true;
    /**
     * 是否保留源文件
     */
    private boolean enableReserveRaw = true;
    /**
     * 压缩后缓存图片目录，非文件目录
     */
    private String cacheDir;
    /**
     * 是否现实压缩进度条
     */
    private boolean showCompressDialog;

    public int getUnCompressMinPixel() {
        return unCompressMinPixel;
    }

    public int getUnCompressNormalPixel() {
        return unCompressNormalPixel;
    }

    public int getMaxPixel() {
        return maxPixel;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isEnablePixelCompress() {
        return enablePixelCompress;
    }

    public boolean isEnableQualityCompress() {
        return enableQualityCompress;
    }

    public boolean isEnableReserveRaw() {
        return enableReserveRaw;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public boolean isShowCompressDialog() {
        return showCompressDialog;
    }

    public static CompressConfig getDefaultConfig(){
        return new CompressConfig();
    }

    private CompressConfig(){}

    private CompressConfig(Builder builder) {
        this.unCompressMinPixel = builder.unCompressMinPixel;
        this.unCompressNormalPixel = builder.unCompressNormalPixel;
        this.maxPixel = builder.maxPixel;
        this.maxSize = builder.maxSize;
        this.enablePixelCompress = builder.enablePixelCompress;
        this.enableQualityCompress = builder.enableQualityCompress;
        this.enableReserveRaw = builder.enableReserveRaw;
        this.cacheDir = builder.cacheDir;
        this.showCompressDialog = builder.showCompressDialog;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private int unCompressMinPixel = 1000;
        private int unCompressNormalPixel = 2000;
        private int maxPixel = 1200;
        private int maxSize = 200*1024; //200kb不压缩
        private boolean enablePixelCompress = true;
        private boolean enableQualityCompress = true;
        private boolean enableReserveRaw = true;
        private String cacheDir;
        private boolean showCompressDialog;

        public Builder setUnCompressMinPixel(int unCompressMinPixel) {
            this.unCompressMinPixel = unCompressMinPixel;
            return this;
        }

        public Builder setUnCompressNormalPixel(int unCompressNormalPixel) {
            this.unCompressNormalPixel = unCompressNormalPixel;
            return this;
        }

        public Builder setMaxPixel(int maxPixel) {
            this.maxPixel = maxPixel;
            return this;
        }

        public Builder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder setEnablePixelCompress(boolean enablePixelCompress) {
            this.enablePixelCompress = enablePixelCompress;
            return this;
        }

        public Builder setEnableQualityCompress(boolean enableQualityCompress) {
            this.enableQualityCompress = enableQualityCompress;
            return this;
        }

        public Builder setEnableReserveRaw(boolean enableReserveRaw) {
            this.enableReserveRaw = enableReserveRaw;
            return this;
        }

        public Builder setCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder setShowCompressDialog(boolean showCompressDialog) {
            this.showCompressDialog = showCompressDialog;
            return this;
        }

        public CompressConfig create(){
            return new CompressConfig(this);
        }
    }
}
