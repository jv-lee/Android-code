#include <jni.h>
#include <string>
#include "VideoChannel.h"
#include "safe_queue.h"
#include "macro.h"

VideoChannel *videoChannel;
SafeQueue<RTMPPacket *> packets;
int isStart = 0;
uint32_t startTime;
pthread_t pid;
int readyPushing = 0;

void callback(RTMPPacket *packet) {
    if (packet) {
        packet->m_nTimeStamp = RTMP_GetTime() - startTime;
        packets.put(packet);
    }
}

void releasePacket(RTMPPacket *&packet) {
    if (packet) {
        RTMPPacket_Free(packet);
        delete packet;
        packet = 0;
    }
}

void *start(void *args) {
    char *url = static_cast<char *>(args);
    RTMP *rtmp = 0;
    rtmp = RTMP_Alloc();
    if (!rtmp) {
        LOGE("RTMP创建失败");
        return NULL;
    }

    RTMP_Init(rtmp);
    int result = RTMP_SetupURL(rtmp, url);
    if (!result) {
        LOGE("RTMP设置地址失败 :%s", url);
        return NULL;
    }
    rtmp->Link.timeout = 5;
    RTMP_EnableWrite(rtmp);
    result = RTMP_Connect(rtmp, 0);
    if (!result) {
        LOGE("连接服务器失败 :%s", url);
        return NULL;
    }
    result = RTMP_ConnectStream(rtmp, 0);
    if (!result) {
        LOGE("连接服务器流失败 :%s", url);
        return NULL;
    }
    startTime = RTMP_GetTime();
    //开始推流
    readyPushing = 1;
    packets.setWork(1);
    RTMPPacket *packet = 0;
    while (readyPushing) {
        //从队列中取出数据包
        packets.get(packet);
        LOGE("从队列中取出一帧数据包");
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
    delete url;
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_examples_live_LivePusher_nativeInit(JNIEnv *env, jobject instance) {
    videoChannel = new VideoChannel;
    videoChannel->setVideoCallback(callback);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_examples_live_LivePusher_nativeSetVideoEncInfo(JNIEnv *env, jobject instance, jint width,
                                                        jint height, jint bitrate, jint fps) {
    if (!videoChannel) {
        return;
    }
    videoChannel->setVideoEncInfo(width, height, fps, bitrate);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_examples_live_LivePusher_nativeStart(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    if (isStart) {
        return;
    }
    isStart = 1;

    //创建url指针变量 将java层的 path存入 url变量中 以防止被回收
    char *url = new char[strlen(path) + 1];
    strcpy(url, path);

    //创建一个线程
    pthread_create(&pid, 0, start, url);

    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_examples_live_LivePusher_nativePushVideo(JNIEnv *env, jobject instance, jbyteArray data_) {
    if (!videoChannel || readyPushing) {
        return;
    }
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    //设置编码数据上传
    videoChannel->encodeData(data);

    env->ReleaseByteArrayElements(data_, data, 0);
}