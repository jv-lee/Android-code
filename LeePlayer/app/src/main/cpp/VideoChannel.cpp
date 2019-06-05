//
// Created by jv.lee on 2019/6/5.
//

#include <pthread.h>
#include "VideoChannel.h"

VideoChannel::VideoChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext) :BaseChannel(id,javaCallHelper,avCodecContext){

}

/**
 * 传入自身 调用自身解码方法 自身解码方法运行在自线程中可以调用到自身的成员变量
 * @param args
 * @return
 */
void *decode(void *args){
    VideoChannel *videoChannel = static_cast<VideoChannel *>(args);
    videoChannel->decodePacket();
}

void VideoChannel::play() {
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;
    //创建线程 即线程执行的函数 run
    pthread_create(&pid_video_play, NULL,decode,this);

}

void VideoChannel::stop() {

}

/**
 * 子线程
 */
void VideoChannel::decodePacket() {
    AVPacket *packet = 0;
    while (isPlaying) {
        //流
    }
}
