//
// Created by jv.lee on 2019/6/5.
//

#include <pthread.h>
#include "VideoChannel.h"

extern "C" {
#include <libavutil/imgutils.h>
#include <libavutil/time.h>
#include <libswscale/swscale.h>
};

VideoChannel::VideoChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext)
        : BaseChannel(id, javaCallHelper, avCodecContext) {
    this->javaCallHelper = javaCallHelper;
    this->avCodecContext = avCodecContext;
}

/**
 * 传入自身 调用自身解码方法 自身解码方法运行在自线程中可以调用到自身的成员变量
 * @param args
 * @return
 */
void *decode(void *args) {
    VideoChannel *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->decodePacket();
    return 0;
}

void *synchronize(void *args) {
    VideoChannel *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->synchronizeFrame();
    return 0;
}

void VideoChannel::play() {
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;
    //创建线程 即线程执行的函数 run
    pthread_create(&pid_video_play, NULL, decode, this);
    pthread_create(&pid_synchronize, NULL, synchronize, this);

}

void VideoChannel::stop() {

}

/**
 * 子线程
 */
void VideoChannel::decodePacket() {
    AVPacket *packet = 0;
    while (isPlaying) {
        //流 -packet 音频，视频 可以单一
        int result = packet_queue.deQueue(packet);
        if (!isPlaying) {
            break;
        }
        if (!result) {
            continue;
        }
        //读取frame
        result = avcodec_send_packet(avCodecContext, packet);
        releaseAvPacket(packet);
        if (result == AVERROR(EAGAIN)) {
            //需要更多数据
            continue;
        } else if (result < 0) {
            //失败
            break;
        }

        AVFrame *frame = av_frame_alloc();
        result = avcodec_receive_frame(avCodecContext, frame);
        //解压数据
        frame_queue.enQueue(frame);
        //延缓解压 10ms毫秒
        while (frame_queue.size() > 100 && isPlaying) {
            av_usleep(1000 * 10);
            continue;
        }
    }
    //播放完毕再次释放packet 防止最后一帧没有及时释放
    releaseAvPacket(packet);
}

void VideoChannel::synchronizeFrame() {
    //初始化转换器上下文， 将视频格式转换为 RGB格式显示到 surfaceView中
    SwsContext *sws_ctx = sws_getContext(
            avCodecContext->width, avCodecContext->height, avCodecContext->pix_fmt,
            avCodecContext->width, avCodecContext->height, AV_PIX_FMT_RGBA,
            SWS_BILINEAR, 0, 0, 0);
    //1s rgba(4)
    uint8_t *dst_data[4];
    int dst_linesize[4];
    //出现问题 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    av_image_alloc(dst_data, dst_linesize, avCodecContext->width, avCodecContext->height,
                   AV_PIX_FMT_RGBA, 1);
    AVFrame *frame = 0;
    int result = 0;
    //开始读取转换为rgba
    while (isPlaying) {
        result = frame_queue.deQueue(frame);
        if (!isPlaying) {
            break;
        }
        if (!result) {
            continue;
        }
        sws_scale(sws_ctx, reinterpret_cast<const uint8_t *const *>(frame->data), frame->linesize,
                  0, frame->height, dst_data, dst_linesize);
        //回调到控制层
        if (renderFrame) {
            renderFrame(dst_data[0], dst_linesize[0], avCodecContext->width,
                        avCodecContext->height);
            //延迟16毫秒
            av_usleep(16 * 1000000);
            //frame数据传rgb回调后 释放frame
            releaseAvFrame(frame);
        }
    }
    //清理内存
    av_freep(&dst_data[0]);
    isPlaying = 0;
    releaseAvFrame(frame);
    sws_freeContext(sws_ctx);
}

//提供接口回调函数
void VideoChannel::setRenderCallback(RenderFrame renderFrame) {
    this->renderFrame = renderFrame;
}
