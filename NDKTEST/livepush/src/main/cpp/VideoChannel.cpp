//
// Created by jv.lee on 2019-05-30.
//
#include <cstring>
#include <librtmp/rtmp.h>
#include "VideoChannel.h"
#include "include/x264.h"

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
    param.i_timebase_num = param.i_fps_num;
    param.i_timebase_den = param.i_fps_den;

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
             //发送非关键帧数据

        }
    }
}

//转换格式为 sps,pps -> RTMP
void VideoChannel::sendSpsPps(uint8_t *sps, uint8_t *pps, int sps_len, int pps_len) {
    int bodySize = 13 + sps_len + 3 + pps_len;
    RTMPPacket *packet = new RTMPPacket;
    RTMPPacket_Alloc(packet, bodySize);

}
