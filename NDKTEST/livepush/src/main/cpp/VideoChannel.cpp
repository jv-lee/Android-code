//
// Created by jv.lee on 2019-05-30.
//
#include <cstring>
#include <librtmp/rtmp.h>
#include "VideoChannel.h"
#include "include/x264.h"
#include "macro.h"



//发送关键帧 和 非关键帧
void VideoChannel::sendFrame(int type, uint8_t *payload, int i_payload) {
    if (payload[2] == 0x00) {
        i_payload -= 4;
        payload += 4;
    } else {
        i_payload -= 3;
        payload += 3;
    }
    int bodySize = 9 + i_payload;
    RTMPPacket *packet = new RTMPPacket;
    RTMPPacket_Alloc(packet, bodySize);

    //非关键帧 0x27
    packet->m_body[0] = 0x27;

    if (type == NAL_SLICE_IDR) {
        packet->m_body[0] = 0x17;
        LOGE("关键帧");
    }

    //类型
    packet->m_body[1] = 0x01;
    //时间戳
    packet->m_body[2] = 0x00;
    packet->m_body[3] = 0x00;
    packet->m_body[4] = 0x00;
    //数据长度 int 4个字节
    packet->m_body[5] = (i_payload >> 24) & 0xff;
    packet->m_body[6] = (i_payload >> 16) & 0xff;
    packet->m_body[7] = (i_payload >> 8) & 0xff;
    packet->m_body[8] = (i_payload) & 0xff;

    //图片数据
    memcpy(&packet->m_body[9], payload, i_payload);
    packet->m_hasAbsTimestamp = 0;
    packet->m_nBodySize = bodySize;
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nChannel = 0x10;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;

    //回调数据包
    videoCallback(packet);

}

void VideoChannel::setVideoEncInfo(int width, int height, int fps, int bitrate) {
    mWidth = width;
    mHeight = height;
    mFps = fps;
    mBitrate = bitrate;
    ySize = width * height;
    uvSize = ySize / 4;

    //初始化x264编码器
    x264_param_t param;
    //设置x264编码默认初始化  参数对象、解码速度最快、0延迟模式
    x264_param_default_preset(&param, "ultrafast", "zerolatency");
    //base_line 3.2编码复杂度
    param.i_level_idc = 32;
    //输入格式 NV21是android默认格式， 但是转换为 i420全设备通用且速度快
    param.i_csp = X264_CSP_I420;

    param.i_width = width;
    param.i_height = height;
    //无b帧 首开 一打开就可以看到
    param.i_bframe = 0;
    //参数i_rc_method表示码率控制，CQP（恒定质量），CRF（恒定码率），ARR(平均码率)
    param.rc.i_rc_method = X264_RC_ABR;
    //码率（比特率，单位Kbps)
    param.rc.i_bitrate = bitrate / 1000;
    //瞬时最大码率
    param.rc.i_vbv_max_bitrate = bitrate / 1000 * 1.2;
    //设置了i_vbv_buffer_size必须设置此参数，码率控制区大小，单位kbps
    param.rc.i_vbv_buffer_size = bitrate / 1000;

    param.i_fps_num = fps;
    param.i_fps_den = 1;
    //时间间隔
    param.i_timebase_den = param.i_fps_den;
    param.i_timebase_num = param.i_fps_num;

    //使用fps 而不是用时间戳来计算帧间距离
    param.b_vfr_input = 0;
    //帧间距离（关键帧） 2s一个关键帧
    param.i_keyint_max = fps * 2;
    //是否复制sps和pps放在每个关键帧的前面 该参数设置是让每个关键帧（I帧）都附带sps/pps
    param.b_repeat_headers = 1;
    //多线程
    param.i_threads = 1;

    //设置编码质量  baseline 最基本 延时最低 质量一般
    x264_param_apply_profile(&param, "baseline");

    //打开解码器
    videoCodec = x264_encoder_open(&param);
    //创建一个 图像显示帧
    pic_in = new x264_picture_t;
    //设置帧视频 类型 及宽高 实列化
    x264_picture_alloc(pic_in, X264_CSP_I420, width, height);

}

void VideoChannel::setVideoCallback(VideoChannel::VideoCallback videoCallback) {
    this->videoCallback = videoCallback;
}

//解码方法  nv21 -> yuv.I420
void VideoChannel::encodeData(int8_t *data) {
    //容器 pic_in 存储一帧数据
    memcpy(pic_in->img.plane[0], data, ySize); //y
    for (int i = 0; i < uvSize; ++i) {
        //uv数据设置
        *(pic_in->img.plane[1] + i) = *(data + ySize + i * 2 + 1); //u 1 3 5 7 9
        *(pic_in->img.plane[2] + i) = *(data + ySize + i * 2); //v  0 2  4 6 8 10
    }
    //解码出来的 yuv数据
    x264_nal_t *pp_nal;
    //编码出来的数据 （多少帧NALU单元）
    int pi_nal;
    x264_picture_t pic_out;
    x264_encoder_encode(videoCodec, &pp_nal, &pi_nal, pic_in, &pic_out);
    int sps_len;
    int pps_len;
    uint8_t sps[100];
    uint8_t pps[100];
    for (int i = 0; i < pi_nal; ++i) {
        if (pp_nal[i].i_type == NAL_SPS) {
            //前面有4个字节的标识  00 00 00 01 ，标识后面才是具体流数据
            sps_len = pp_nal->i_payload - 4;
            memcpy(sps, pp_nal[i].p_payload + 4, sps_len);
        } else if (pp_nal[i].i_type == NAL_PPS) {
            pps_len = pp_nal->i_payload - 4;
            memcpy(pps, pp_nal[i].p_payload + 4, pps_len);
            sendSpsPps(sps, pps, sps_len, pps_len);
        } else {
            //发送关键帧数据 、 非关键帧数据
            sendFrame(pp_nal[i].i_type, pp_nal[i].p_payload, pp_nal[i].i_payload);
        }
    }
}

//转换格式为 sps,pps -> RTMP
void VideoChannel::sendSpsPps(uint8_t *sps, uint8_t *pps, int sps_len, int pps_len) {
    int bodySize = 13 + sps_len + 3 + pps_len;
    RTMPPacket *packet = new RTMPPacket;
    RTMPPacket_Alloc(packet, bodySize);

    int i = 0;
    //固定头
    packet->m_body[i++] = 0x17;
    //类型
    packet->m_body[i++] = 0x00;
    //composition time 0x000000
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;

    //版本
    packet->m_body[i++] = 0x01;
    //编码规格
    packet->m_body[i++] = sps[1];
    packet->m_body[i++] = sps[2];
    packet->m_body[i++] = sps[3];
    packet->m_body[i++] = 0xFF;

    //整个sps
    packet->m_body[i++] = 0xE1;
    //sps长度
    packet->m_body[i++] = (sps_len >> 8) & 0xff;
    packet->m_body[i++] = sps_len & 0xff;
    memcpy(&packet->m_body[i], sps, sps_len);
    i += sps_len;

    //pps
    packet->m_body[i++] = 0x01;
    packet->m_body[i++] = (pps_len >> 8) & 0xff;
    packet->m_body[i++] = (pps_len) & 0xff;
    memcpy(&packet->m_body[i], pps, pps_len);

    //视频
    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nBodySize = bodySize;
    //分配一个管道，（尽量避开rtmp.c中使用的）
    packet->m_nChannel = 10;

    //sps pps 没有时间戳
    packet->m_nTimeStamp = 0;
    //不使用绝对时间
    packet->m_hasAbsTimestamp = 0;

    packet->m_headerType = RTMP_PACKET_SIZE_MEDIUM;
    if (videoCallback) {
        videoCallback(packet);
    }
}


