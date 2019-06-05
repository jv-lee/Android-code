#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
#include "PlayControl.h"
#include "JavaCallHelper.h"
extern "C" {
#include <libavformat/avformat.h>
}

JavaCallHelper *javaCallHelper;
ANativeWindow *window = 0;
PlayControl *playControl;


/**
 * jni会自动调用构建 jvm实列
 * native子线程回调java层需要绑定 jvm实列
 */
JavaVM *javaVM = NULL;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
    javaVM = vm;
    return JNI_VERSION_1_4;
}

/**
 * native开始准备
 * 调用播放控制器 准备 音视频模块初始化
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_examples_explay_ExPlay_nativePrepare(JNIEnv *env, jobject instance, jstring dataSource_) {
    const char *dataSource = env->GetStringUTFChars(dataSource_, 0);
    javaCallHelper = new JavaCallHelper(javaVM, env, instance);
    playControl = new PlayControl(javaCallHelper, dataSource);
    playControl->prepare();

    env->ReleaseStringUTFChars(dataSource_, dataSource);
}

/**
 * 准备完成 回调开始播放
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_examples_explay_ExPlay_nativeStart(JNIEnv *env, jobject instance) {
    //进入播放状态
    if (playControl) {
        playControl->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_examples_explay_ExPlay_nativeSetSurface(JNIEnv *env, jobject instance, jobject surface) {
    //存在即释放
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
    //创建新的窗口用于视频显示
    window = ANativeWindow_fromSurface(env, surface);
}