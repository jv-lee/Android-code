//
// Created by jv.lee on 2019-05-29.
//

#include <jni.h>
#include <string>
#include <x264.h>
#include <pthread.h>
#include "librtmp/rtmp.h"
#include "VideoChannel.h"
#include <android/log.h>
#include <safe_queue.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"lee",__VA_ARGS__)
#define DELETE(obj) if(obj){ delete obj; obj = 0;}

//防止用户重复点击开始直播，导致重新初始化 设置一个start标识符
int isStart = 0;
VideoChannel *videoChannel;
pthread_t pid;
uint32_t start_time;
int readyPushing = 0;
//数据包队列
SafeQueue<RTMPPacket *> packets;

//释放packet
void releasePacket(RTMPPacket *&packet){
    if (packet) {
        RTMPPacket_Free(packet);
        delete packet;
        packet = 0;
    }
}

//pid 线程回调函数指针
void *start(void *args) {
    char *url = static_cast<char *>(args);
    RTMP *rtmp = 0;
    rtmp = RTMP_Alloc();
    if (!rtmp) {
        LOGE("alloc rtmp失败");
        return NULL;
    }
    //初始化rtmp
    RTMP_Init(rtmp);
    //设置rtmp URL 地址
    int result = RTMP_SetupURL(rtmp, url);
    if (!result) {
        LOGE("设置地址失败：%s", url);
        return NULL;
    }

    //设置连接时间
    rtmp->Link.timeout = 5;
    //设置可写入模式
    RTMP_EnableWrite(rtmp);
    //开始连接服务器
    result = RTMP_Connect(rtmp, 0);
    if (!result) {
        LOGE("连接服务器失败：%s", url);
        return NULL;
    }

    //连接或获取连接流
    result = RTMP_ConnectStream(rtmp, 0);
    if (!result) {
        LOGE("连接服务器流失败：%s", url);
        return NULL;
    }

    //获取开始连接时间
    start_time = RTMP_GetTime();
    //开始推流
    readyPushing = 1;
    RTMPPacket *packet = 0;
    while (readyPushing) {
        //队列中获取数据 packet数据包
        packets.get(packet);
        LOGE("取出一帧数据");
        if (!readyPushing) {
            break;
        }
        if (!packet) {
            continue;
        }
        //发送数据包
        packet->m_nInfoField2 = rtmp->m_stream_id;
        result = RTMP_SendPacket(rtmp, packet, 1);

        //释放packet
        releasePacket(packet);
    }

    //设置标识状态 清空释放引用
    isStart = 0;
    readyPushing = 0;
    packets.setWork(0);
    packets.clear();
    if (rtmp) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
    delete (url);

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_lee_code_livepush_MainActivity_helloJni(JNIEnv *env, jobject instance) {

    x264_picture_t *x264_picture = new x264_picture_t;
    RTMP_Alloc();

    std::string hello = "Hello form C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_nativeInit(JNIEnv *env, jobject instance) {
    videoChannel = new VideoChannel;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_nativeSetVideoEncInfo(JNIEnv *env, jobject instance, jint width, jint height,
                                                            jint fps, jint bitrate) {
    if (!videoChannel) {
        return;
    }

    videoChannel->setVideoEncInfo(width, height, fps, bitrate);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_nativeStart(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    if (isStart) {
        return;
    }
    isStart = 1;

    //临时变量url  把path地址保存到临时变量中 以防path被回收
    char *url = new char[strlen(path) + 1];
    strcpy(url, path);

    //创建一个线程
    pthread_create(&pid, 0, start, url);

    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_livepush_LivePusher_nativePushVideo(JNIEnv *env, jobject instance, jbyteArray data_) {
    if (!videoChannel || !readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    videoChannel->encodeData(data);



    env->ReleaseByteArrayElements(data_, data, 0);
}