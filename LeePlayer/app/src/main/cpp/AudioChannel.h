//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_AUDIOCHANNEL_H
#define EXPLAY_AUDIOCHANNEL_H

#include <SLES/OpenSLES_Android.h>
#include "BaseChannel.h"
extern "C" {
#include <libavutil/time.h>
#include <libswresample/swresample.h>
}

class AudioChannel : public BaseChannel {

public:
    AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext,AVRational time_base);

    virtual ~AudioChannel();

    void play();

    void stop();

    void initOpenSL();

    void decode();

    int getPcm();

    uint8_t *buffer;

private:
    pthread_t pid_audio_play;
    pthread_t pid_audio_decode;
    SwrContext *swr_ctx = NULL;
    int out_channels;
    int out_samplesize;
    int out_sample_rate;
};


#endif //EXPLAY_AUDIOCHANNEL_H
