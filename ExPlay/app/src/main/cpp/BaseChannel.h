//
// Created by Raytine on 2019/6/5.
//

#ifndef EXPLAY_BASECHANNEL_H
#define EXPLAY_BASECHANNEL_H


#include <libavcodec/avcodec.h>
#include "safe_queue.h"

class BaseChannel{
public:
    SafeQueue<AVPacket *>  *packet_queue;
    SafeQueue<AVFrame *> frame_queue;
};

#endif //EXPLAY_BASECHANNEL_H
