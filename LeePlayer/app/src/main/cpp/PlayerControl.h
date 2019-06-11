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
    PlayControl(JavaCallHelper *javaCallHelper, const char *dataSource);

    ~PlayControl();

    void prepare();

    void prepareControl();

    void start();

    void startControl();

    void setRenderCallback(RenderFrame renderFrame);

private:
    bool isPlaying;
    char *url;
    pthread_t pid_prepare;//准备线程运行结束即销毁
    pthread_t pid_start;//解码线程一直存在 直到播放完毕
    AVFormatContext *formatContext;
    JavaCallHelper *javaCallHelper;
    VideoChannel *videoChannel;
    AudioChannel *audioChannel;
    RenderFrame renderFrame;
};


#endif //EXPLAY_PLAYCONTROL_H
