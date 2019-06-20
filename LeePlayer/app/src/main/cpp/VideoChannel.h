//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_VIDEOCHANNEL_H
#define EXPLAY_VIDEOCHANNEL_H


#include "JavaCallHelper.h"
#include "BaseChannel.h"
#include "AudioChannel.h"

typedef void (*RenderFrame)(uint8_t *, int, int, int);

class VideoChannel : public BaseChannel {
public:
    VideoChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext,AVRational time_base);
    virtual ~VideoChannel();
    virtual void play();

    virtual void stop();

    void decodePacket();

    void synchronizeFrame();

    void setRenderCallback(RenderFrame renderFrame);

    void setFps(int fps);

private:
    pthread_t pid_video_play;
    pthread_t pid_synchronize;
    RenderFrame renderFrame;
    int fps;

public:
    AudioChannel *audioChannel;
};


#endif //EXPLAY_VIDEOCHANNEL_H
