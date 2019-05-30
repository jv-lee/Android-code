//
// Created by jv.lee on 2019-05-29.
//

#include <jni.h>
#include <string>
#include <x264.h>
#include "librtmp/rtmp.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lee_code_livepush_MainActivity_helloJni(JNIEnv *env, jobject instance) {

    x264_picture_t* x264_picture = new x264_picture_t;
    RTMP_Alloc();

    std::string hello = "Hello form C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_native_1init(JNIEnv *env, jobject instance) {


}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_native_1setVideoEncInfo(JNIEnv *env, jobject instance, jint width, jint height,
                                                              jint fps, jint bitrate) {


}