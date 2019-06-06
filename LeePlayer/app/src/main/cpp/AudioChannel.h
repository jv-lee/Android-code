//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_AUDIOCHANNEL_H
#define EXPLAY_AUDIOCHANNEL_H

#include "JavaCallHelper.h"
#include "BaseChannel.h"

extern "C"{
#include <libavcodec/avcodec.h>
};

class AudioChannel :public BaseChannel{

public:
    AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext);
    void play();
    void stop();
};


#endif //EXPLAY_AUDIOCHANNEL_H
