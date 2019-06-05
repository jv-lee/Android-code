//
// Created by jv.lee on 2019/6/5.
//

#include "PlayControl.h"
#include "JavaCallHelper.h"
#include "macro.h"

void *prepareThread(void *args){
    PlayControl *playControl = static_cast<PlayControl *>(args);
    playControl->prepareControl();
}

PlayControl::PlayControl(JavaCallHelper *javaCallHelper,const char *dataSource) {
    url = new char[strlen(dataSource) + 1];
    this->javaCallHelper = javaCallHelper;
    //因为在上一层已经释放了， 所以在控制层再次拷贝url数据
    strcpy(url, dataSource);
}

PlayControl::~PlayControl() {

}

void PlayControl::prepare() {
    pthread_create(&pid_prepare, NULL,prepareThread,this);
}

/**
 * 子线程执行方法
 */
void PlayControl::prepareControl() {
    //引入用线程
    this->pid_prepare;
    avformat_network_init();
    formatContext = avformat_alloc_context();
    //1.打开url
    AVDictionary *opts = NULL;
    //设置超时3秒
    av_dict_set(&opts, "timeout", "3000000", 0);
    //强制指定AVFormatContext中的AVInputFormat的 ，这个参数一般可以为NULL，这样ffmpeg可以自动检测AVInputFormat ：输入封装格式 av_find_input_format("avi")
    int result = avformat_open_input(&formatContext, url, NULL, &opts);
    if (result != 0) {
        javaCallHelper->onError(THREAD_CHILD,FFMPEG_CAN_NOT_OPEN_URL);
        return;
    }
    //2.查找流
    if (avformat_find_stream_info(formatContext, NULL) < 0) {
        if (javaCallHelper) {
            javaCallHelper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_FIND_STREAMS);
        }
        return;
    }

    for (int i = 0; i < formatContext->nb_streams; ++i) {
        AVCodecParameters *codecpar = formatContext->streams[i]->codecpar;
        //找到解码器
        AVCodec *dec = avcodec_find_decoder(codecpar->codec_id);

    }

}


