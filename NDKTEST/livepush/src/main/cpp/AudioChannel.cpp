//
// Created by Raytine on 2019/6/4.
//

#include <librtmp/rtmp.h>
#include <cstring>
#include "AudioChannel.h"
#include "faac.h"

//音频首帧提前传入服务器缓存 ， 为了后面可以根据该帧格式来解码 音频流
RTMPPacket *AudioChannel::getAudioTag() {
    u_char *buf;
    u_long len;
    //获取当前的编码器信息 -> 解码参考编码信息
    faacEncGetDecoderSpecificInfo(audioCodec, &buf, &len);
    int bodySize = 2 + len;
    RTMPPacket *packet = new RTMPPacket;
    RTMPPacket_Alloc(packet, bodySize);
    //双声道
    packet->m_body[0] = 0xAF;
    //单声道
    if (mChannels == 1) {
        packet->m_body[0] = 0xAE;
    }
    packet->m_body[1] = 0x00;
    //拷贝数据传输
    memcpy(&packet->m_body[2], buf, len);

    packet->m_hasAbsTimestamp = 0;
    packet->m_nBodySize = bodySize;
    packet->m_packetType = RTMP_PACKET_TYPE_AUDIO;
    packet->m_nChannel = 0x11;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    return packet;
}

//不断调用推流
void AudioChannel::encodeData(int8_t *data) {
    int bytelen = faacEncEncode(audioCodec, reinterpret_cast<int32_t *>(data), inputSamples, buffer, maxOutputBytes);
    if (bytelen > 0) {
        RTMPPacket *packet = new RTMPPacket;

        int bodySize = 2 + bytelen;
        RTMPPacket_Alloc(packet, bodySize);
        packet->m_body[0] = 0xAF;
        //判断是否为单声道
        if (mChannels == 1) {
            packet->m_body[0] = 0xAE;
        }
        //编码出的声音 0x01
        packet->m_body[1] = 0x01;

        //编码之后aac数据 内容 不固定
        memcpy(&packet->m_body[2], buffer, bytelen);
        //aac
        packet->m_hasAbsTimestamp = 0;
        packet->m_nBodySize = bodySize;
        packet->m_packetType = RTMP_PACKET_TYPE_AUDIO;
        packet->m_nChannel = 0x11;
        packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
        audioCallback(packet);
    }
}

//初始化编码器 采样率、通道数
void AudioChannel::setAudioEncInfo(int samplesInHz, int channles) {
    //设置最大缓冲区大小
    audioCodec = faacEncOpen(samplesInHz, channles, &inputSamples, &maxOutputBytes);
    //设置参数
    faacEncConfigurationPtr config = faacEncGetCurrentConfiguration(audioCodec);
    config->mpegVersion = MPEG4;
    config->aacObjectType = LOW;
    config->inputFormat = FAAC_INPUT_16BIT;
    config->outputFormat = 0;//原始数据
    //设置完后重新设置编码器 及 配置
    faacEncSetConfiguration(audioCodec, config);
    buffer = new u_char[maxOutputBytes];
}

int AudioChannel::getInputSamples() {
    return static_cast<int>(inputSamples);
}

void AudioChannel::setAudioCallback(AudioChannel::AudioCallback audioCallback) {
    this->audioCallback = audioCallback;
}
