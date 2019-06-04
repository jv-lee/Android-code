#include <jni.h>
#include <string>
#include <x264.h>
#include "librtmp/rtmp.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_lee_code_livepush_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    RTMP *rtmp = new RTMP;
    RTMP_Init(rtmp);
    x264_picture_t *p = new x264_picture_t;

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
