//
// Created by jv.lee on 2019-06-05.
//

#ifndef LEEPLAYER_BASECHANNEL_H
#define LEEPLAYER_BASECHANNEL_H

extern "C" {
//#include <libavcodec/avcodec.h>
#include "include/libavutil/rational.h"
#include "include/libavcodec/avcodec.h"
#include "include/libavutil/frame.h"
};

#include "safe_queue.h"
#include "JavaCallHelper.h"
#include "macro.h"


class BaseChannel {
public:
    BaseChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext)
            : channelID(id), javaCallHelper(javaCallHelper), avCodecContext(avCodecContext) {
    };

    virtual ~BaseChannel() {
        if (avCodecContext) {
            avcodec_close(avCodecContext);
            avcodec_free_context(&avCodecContext);
            avCodecContext = 0;
        }
        packet_queue.clear();
        frame_queue.clear();
        LOGE("释放channel:%d %d", packet_queue.size(), frame_queue.size());
    };

    virtual void play() = 0;

    virtual void stop() = 0;


    SafeQueue<AVPacket *> packet_queue;
    SafeQueue<AVFrame *> frame_queue;
    volatile int channelID;
    volatile bool isPlaying;
    AVCodecContext *avCodecContext;
    JavaCallHelper *javaCallHelper;
};


#endif //LEEPLAYER_BASECHANNEL_H
