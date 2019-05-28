package com.lee.code.bsdiff;

/**
 * @author jv.lee
 * @date 2019-05-28
 */
public class BsPatcher {
    static {
        System.loadLibrary("native-lib");
    }


    /**
     *
     * @param oldApk 旧版本安装包
     * @param patch 查分包
     * @param output 合成后新版本apk 输出路径
     */
    public static native void bsPatch(String oldApk, String patch, String output);

}
