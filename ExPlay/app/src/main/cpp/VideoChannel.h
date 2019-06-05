//
// Created by Raytine on 2019/6/5.
//

#ifndef EXPLAY_VIDEOCHANNEL_H
#define EXPLAY_VIDEOCHANNEL_H

#include <libavcodec/avcodec.h>
#include "JavaCallHelper.h"

class VideoChannel {
public:
    VideoChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext);

    void play();
};


#endif //EXPLAY_VIDEOCHANNEL_H
