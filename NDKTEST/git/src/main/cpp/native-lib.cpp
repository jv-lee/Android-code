#include <jni.h>
#include <string>
#include <malloc.h>
#include <cstring>
#include "gif_lib.h"
#include <android/log.h>
#include <android/bitmap.h>

#define argb(a, r, g, b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) <<16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)

typedef struct GifBean {
    //当前播放帧数
    int current_frame;
    //总帧数
    int total_frame;
    //延迟时间数组
    int *dealys;
} GifBean;

//绘制一张图片 渲染到bitmap
void drawFrame(GifFileType *gifFileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels) {
    SavedImage savedImage = gifFileType->SavedImages[gifBean->current_frame];

    //整幅图片首地址
    int *px = (int *) pixels;
    int pointPixel;
    GifImageDesc frameInfo = savedImage.ImageDesc;
    //压缩数据
    GifByteType gifByteType;
    //rgb数据 压缩工具
    ColorMapObject* colorMapObject = frameInfo.ColorMap;

    //Bitmap向下偏移
    px = (int *) ((char *) px + info.stride * frameInfo.Top);
    //每一行的首地址
    int *line;
    for (int y = frameInfo.Top; y < frameInfo.Top + frameInfo.Height; ++y) {
        line = px;
        for (int x = frameInfo.Left; x < frameInfo.Left + frameInfo.Width; ++x) {
            //拿到每一个坐标的位置 索引 -> 数据
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
            //索引 rgb lzw压缩字典 （）缓存在一个字典

            //解压
            gifByteType = savedImage.RasterBits[pointPixel];
            GifColorType gifColorType = colorMapObject->Colors[gifByteType];
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        px = (int *) ((char *) px + info.stride);
    }
}

//获取gif路径
extern "C"
JNIEXPORT jlong JNICALL
Java_com_lee_code_git_GifHandler_loadPath(JNIEnv *env, jobject instance, jstring path_) {
    //转换为c类型
    const char *path = env->GetStringUTFChars(path_, 0);
    int error;

    //打开一个gif文件获取 gif结构体
    GifFileType *gifFileType = DGifOpenFileName(path, &error);
    //通知刷新绑定
    DGifSlurp(gifFileType);

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
    gifFileType->UserData = gifBean;
    gifBean->current_frame = 0;
    gifBean->total_frame = gifFileType->ImageCount;

    ExtensionBlock *ext;
    //遍历每一帧获取延时时间 设置延时时间数组
    for (int i = 0; i < gifFileType->ImageCount; ++i) {
        SavedImage frame = gifFileType->SavedImages[i];
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            //获取图像处理扩展块
            if (frame.ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                ext = &frame.ExtensionBlocks[j];
                break;
            }
        }
        if (ext) {
            int frame_delay = 10 * (ext->Bytes[1] | (ext->Bytes[2] << 8));
            gifBean->dealys[i] = frame_delay;
        }
    }

    //通知刷新绑定
//    DGifSlurp(gifFileType);

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
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    GifBean *gifBean = (GifBean *) gifFileType->UserData;
    AndroidBitmapInfo info;
    //入参出参对象
    AndroidBitmap_getInfo(env, bitmap, &info);

    //加锁渲染
    void *pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    drawFrame(gifFileType, gifBean, info, pixels);
    gifBean->current_frame += 1;
    if (gifBean->current_frame >= gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    return gifBean->dealys[gifBean->current_frame];
}