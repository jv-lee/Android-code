//
// Created by Raytine on 2019/6/5.
//

#ifndef EXPLAY_AUDIOCHANNEL_H
#define EXPLAY_AUDIOCHANNEL_H

#include <libavcodec/avcodec.h>
#include "JavaCallHelper.h"

class AudioChannel {

public:
    AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext);
    void play();
};


#endif //EXPLAY_AUDIOCHANNEL_H
