//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_PLAYCONTROL_H
#define EXPLAY_PLAYCONTROL_H

#include <pthread.h>
#include <android/native_window_jni.h>
#include "JavaCallHelper.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/time.h>
}

/**
 * 控制层
 */
class PlayControl {
public:
    PlayControl(JavaCallHelper *javaCallHelper,const char *dataSource);
    ~PlayControl();
    void prepare();
    void prepareControl();

private:
    char *url;
    pthread_t pid_prepare;
    AVFormatContext *formatContext;
    JavaCallHelper *javaCallHelper;
};


#endif //EXPLAY_PLAYCONTROL_H
