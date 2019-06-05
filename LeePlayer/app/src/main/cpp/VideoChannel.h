//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_VIDEOCHANNEL_H
#define EXPLAY_VIDEOCHANNEL_H


#include "JavaCallHelper.h"
#include "BaseChannel.h"

extern "C"{
#include <libavcodec/avcodec.h>
};

class VideoChannel :public BaseChannel{
public:
    VideoChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext);
    virtual void play();
    virtual void stop();
    void decodePacket();

private:
    pthread_t pid_video_play;
    pthread_t pid_synchronize;
};


#endif //EXPLAY_VIDEOCHANNEL_H
