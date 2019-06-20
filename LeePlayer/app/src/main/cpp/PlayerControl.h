//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_PLAYCONTROL_H
#define EXPLAY_PLAYCONTROL_H

#include <pthread.h>
#include <android/native_window.h>
#include "VideoChannel.h"
#include "AudioChannel.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/time.h>
}

/**
 * 控制层
 */
class PlayControl {
public:
    int duration;

    PlayControl(JavaCallHelper *javaCallHelper, const char *dataSource);

    ~PlayControl();

    void prepare();

    void prepareControl();

    void start();

    void startControl();

    void setRenderCallback(RenderFrame renderFrame);

    int getDuration();

    void seek(int progress);

    void stop();

public:
    bool isPlaying;
    char *url;
    pthread_t pid_prepare;//准备线程运行结束即销毁
    pthread_t pid_start;//解码线程一直存在 直到播放完毕
    pthread_t pid_stop;//释放线程
    AVFormatContext *formatContext;
    JavaCallHelper *javaCallHelper;
    VideoChannel *videoChannel;
    AudioChannel *audioChannel;
    RenderFrame renderFrame;
    bool isSeek = 0;
    pthread_mutex_t seekMutex;
};


#endif //EXPLAY_PLAYCONTROL_H
