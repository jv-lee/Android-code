#include <jni.h>
#include <string>
#include "gif_lib.h"

typedef struct GifBean {
    //当前播放帧数
    int current_frame;
    //总帧数
    int total_frame;
    //延迟时间数组
    int *dealys;
} GifBean;

//获取gif图路径
extern "C"
JNIEXPORT jlong JNICALL
Java_com_lee_code_git_GifHandler_loadPath(JNIEnv *env, jobject instance, jstring path_) {
    //转换为c类型
    const char *path = env->GetStringUTFChars(path_, 0);
    int error;

    //打开一个gif文件获取 gif结构体
    GifFileType *gifFileType = DGifOpenFileName(path, &error);

    //创建对象 相当于java中的new  只是在c中需要自己开辟内存  malloc创建 通过size
    GifBean *gifBean = (GifBean *) malloc(sizeof(GifBean));
    //清空内存初始化 清空通过 memset
    memset(gifBean, 0, sizeof(GifBean));

    //初始化对象数据bean
    gifFileType->UserData = gifBean;

    //初始化延迟数组 int类型数组 乘以总帧数 初始化dealys数据
    gifBean->dealys = (int *) malloc(sizeof(int) * gifFileType->ImageCount);
    memset(gifBean->dealys, 0, sizeof(int) * gifFileType->ImageCount);

    //获取时间


    //通知刷新绑定
    DGifSlurp(gifFileType);


    env->ReleaseStringUTFChars(path_, path);

    //返回一个gif结构体的内存地址
    return (jlong) gifFileType;
}

//获取gif图宽度
extern "C"
JNIEXPORT jint JNICALL
Java_com_lee_code_git_GifHandler_getWidth(JNIEnv *env, jobject instance, jlong ndkGif) {
    //通过内存地址强转获取gif结构体指针对象
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SWidth;
}

//获取gif图高度
extern "C"
JNIEXPORT jint JNICALL
Java_com_lee_code_git_GifHandler_getHeight(JNIEnv *env, jobject instance, jlong ndkGif) {
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SHeight;
}

//通知应用更新gif刷新
extern "C"
JNIEXPORT jint JNICALL
Java_com_lee_code_git_GifHandler_updateFrame(JNIEnv *env, jobject instance, jlong ndkGif, jobject bitmap) {

}