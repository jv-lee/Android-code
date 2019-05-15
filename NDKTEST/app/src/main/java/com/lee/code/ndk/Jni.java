package com.lee.code.ndk;

/**
 * @author jv.lee
 * @date 2019-05-16
 */
public class Jni {
    {
        System.loadLibrary("hello-jni");
    }

    native int nativeTest();
}
