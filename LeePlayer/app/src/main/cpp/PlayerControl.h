//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_PLAYCONTROL_H
#define EXPLAY_PLAYCONTROL_H

#include <pthread.h>
#include <android/native_window_jni.h>
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

private:
    bool isPlaying;
    char *url;
    pthread_t pid_prepare;
    AVFormatContext *formatContext;
    JavaCallHelper *javaCallHelper;
    VideoChannel *videoChannel;
    AudioChannel *audioChannel;
};


#endif //EXPLAY_PLAYCONTROL_H
