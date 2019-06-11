//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_AUDIOCHANNEL_H
#define EXPLAY_AUDIOCHANNEL_H

#include <SLES/OpenSLES_Android.h>
#include "BaseChannel.h"

class AudioChannel : public BaseChannel {

public:
    AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext);

    void play();

    void stop();

    void initOpenSL();

private:
    pthread_t pid_audio_play;
    pthread_t pid_audio_decode;
};


#endif //EXPLAY_AUDIOCHANNEL_H
