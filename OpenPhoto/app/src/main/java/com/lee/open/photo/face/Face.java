package com.lee.open.photo.face;

import android.util.Log;

import java.util.Arrays;

/**
 * @author jv.lee
 * @date 2019/6/27.
 * @description Native反射生成人脸数据
 */
public class Face {
    public float[] faceRects;

    /**
     * 人脸宽度
     */
    public int width;

    /**
     * 人脸高度
     */
    public int height;

    /**
     * 检测图片宽度
     */
    public int imgWidth;

    /**
     * 检测图片高度
     */
    public int imgHeight;

    public Face(float[] faceRects, int width, int height, int imgWidth, int imgHeight) {
        this.faceRects = faceRects;
        this.width = width;
        this.height = height;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        Log.i("lee >>>", "Face:" + toString());
    }

    @Override
    public String toString() {
        return "Face{" +
                "faceRects=" + Arrays.toString(faceRects) +
                ", width=" + width +
                ",height=" + height +
                ",imgWidth=" + imgWidth +
                ",imgHeight=" + imgHeight +
                "}";
    }
}
