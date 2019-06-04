//
// Created by Raytine on 2019/6/4.
//

#ifndef NDKTEST_AUDIOCHANNEL_H
#define NDKTEST_AUDIOCHANNEL_H


#include <cstdint>
#include <faac.h>
#include <sys/types.h>
#include <jni.h>

class AudioChannel {
    typedef void(*AudioCallback)(RTMPPacket *packet);

public:
    void setAudioEncInfo(int samplesInHz, int channles);

    void encodeData(int8_t *data);

    jint getInputSamples();

    RTMPPacket* getAudioTag();

    void setAudioCallback(AudioCallback audioCallback);
private:
    AudioCallback audioCallback;
    int mChannels;
    faacEncHandle audioCodec;
    u_long inputSamples;
    u_long maxOutputBytes;
    u_char *buffer = 0;
};


#endif //NDKTEST_AUDIOCHANNEL_H
