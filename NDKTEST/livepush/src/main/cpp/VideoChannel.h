//
// Created by jv.lee on 2019-05-30.
//

#ifndef NDKTEST_VIDEOCHANNEL_H
#define NDKTEST_VIDEOCHANNEL_H


#include <x264.h>
#include <jni.h>

class VideoChannel {

public:
    void setVideoEncInfo(jint width, jint height, jint fps, jint bitrate);

    void encodeData(int8_t *data);

private:
    int mWidth;
    int mHeight;
    int mFps;
    int mBitrate;
    int ySize;
    int uvSize;
    x264_t *videoCodec;
    x264_picture_t *pic_in;

    void sendSpsPps(uint8_t sps[100], uint8_t pps[100], int len, int pps_len);
};


#endif //NDKTEST_VIDEOCHANNEL_H
