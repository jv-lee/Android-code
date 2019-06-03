//
// Created by Raytine on 2019/6/3.
//

#ifndef LIVEDEMO_VIDEOCHANNEL_H
#define LIVEDEMO_VIDEOCHANNEL_H

#include <x264.h>
#include <jni.h>
#include <librtmp/rtmp.h>

class VideoChannel {
    //回调方法 函数指针
    typedef void (*VideoCallback)(RTMPPacket *packet);

public:
    void setVideoEncInfo(jint width, jint height, jint fps, jint bitrate);

    void encodeData(int8_t *data);

    void setVideoCallback(VideoCallback videoCallback);

private:
    int mWidth;
    int mHeight;
    int mFps;
    int mBitrate;
    int ySize;
    int uvSize;
    x264_t *videoCodec;
    x264_picture_t *pic_in;
    VideoCallback videoCallback;

    void sendSpsPps(uint8_t sps[100], uint8_t pps[100], int sps_len, int pps_len);

    void sendFrame(int type, uint8_t *payload, int i_payload);
};


#endif //LIVEDEMO_VIDEOCHANNEL_H
