#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
#include "PlayerControl.h"
#include "JavaCallHelper.h"

extern "C" {
#include <libavcodec/avcodec.h>
}

JavaCallHelper *javaCallHelper;
ANativeWindow *window = 0;
PlayControl *playControl;


/**
 * jni会自动调用构建 jvm实列
 * native子线程回调java层需要绑定 jvm实列
 */
JavaVM *javaVM = NULL;
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    return JNI_VERSION_1_4;
}

void renderFrame(uint8_t *data, int linesize, int width, int height) {
    //渲染
    ANativeWindow_setBuffersGeometry(window, width, height, WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer window_buffer;
    if (ANativeWindow_lock(window, &window_buffer, 0)) {
        ANativeWindow_release(window);
        window = 0;
        return;
    }
    //缓冲区 window_data[0] = 255;
    uint8_t *window_data = static_cast<uint8_t *>(window_buffer.bits);
    //stride一行的像素  r（一个字节） g b a    *4
    int window_linesize = window_buffer.stride * 4;
    //数据源
    uint8_t *src_data = data;
    //一行一行拷贝
    for (int i = 0; i < window_buffer.height; ++i) {
        //拷贝目的地， 数据源，目的地一行的size  进行偏移
        //window_data第一行：  i*window_linesize 一行一行偏移下去
        //src_data + i * linesize  :一行一行的数据源偏移下去  1* linesize , 2*linesize 3*linesize
        memcpy(window_data + i * window_linesize, src_data + i * linesize, window_linesize);
    }
    ANativeWindow_unlockAndPost(window);
}

/**
 * native开始准备
 * 调用播放控制器 准备 音视频模块初始化
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_player_LeePlayer_nativePrepare(JNIEnv *env, jobject instance,
                                                 jstring dataSource_) {
    const char *dataSource = env->GetStringUTFChars(dataSource_, 0);
    javaCallHelper = new JavaCallHelper(javaVM, env, instance);
    playControl = new PlayControl(javaCallHelper, dataSource);
    playControl->setRenderCallback(renderFrame);
    playControl->prepare();

    env->ReleaseStringUTFChars(dataSource_, dataSource);
}

/**
 * 准备完成 回调开始播放
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_player_LeePlayer_nativeStart(JNIEnv *env, jobject instance) {
    //进入播放状态
    if (playControl) {
        playControl->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_player_LeePlayer_nativeSetSurface(JNIEnv *env, jobject instance,
                                                    jobject surface) {
    //存在即释放
    if (window) {
        ANativeWindow_release(window);
        window = 0;
    }
    //创建新的窗口用于视频显示
    window = ANativeWindow_fromSurface(env, surface);
}