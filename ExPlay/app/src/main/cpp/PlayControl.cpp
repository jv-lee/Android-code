//
// Created by jv.lee on 2019/6/5.
//

#include "PlayControl.h"
#include "JavaCallHelper.h"
#include "macro.h"

/**
 * 线程函数
 *  当前类直接引用自身调用线程执行
 * @param args
 * @return
 */
void *prepareThread(void *args) {
    PlayControl *playControl = static_cast<PlayControl *>(args);
    playControl->prepareControl();
    return 0;
}

PlayControl::PlayControl(JavaCallHelper *javaCallHelper, const char *dataSource) {
    url = new char[strlen(dataSource) + 1];
    this->javaCallHelper = javaCallHelper;
    //因为在上一层已经释放了， 所以在控制层再次拷贝url数据
    strcpy(url, dataSource);
}

PlayControl::~PlayControl() {

}

/**
 * 控制层 播放模块准备函数
 * 初始化音视频模块
 */
void PlayControl::prepare() {
    pthread_create(&pid_prepare, NULL, prepareThread, this);
}

/**
 * 子线程执行方法
 * 准备函数
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
        javaCallHelper->onError(THREAD_CHILD, FFMPEG_CAN_NOT_OPEN_URL);
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
        if (!dec) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_FIND_DECODER_FAIL);
            }
            return;
        }
        //创建上下文
        AVCodecContext *codecContext = avcodec_alloc_context3(dec);
        if (!codecContext) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_ALLOC_CODEC_CONTEXT_FAIL);
            }
            return;
        }
        //复制参数
        if (avcodec_parameters_to_context(codecContext, codecpar) < 0) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL);
            }
            return;
        }
        //打开解码器
        if (avcodec_open2(codecContext, dec, 0) != 0) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_OPEN_DECODER_FAIL);
            }
            return;
        }

        //获取音视频模块实例
        if (codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            //音频
            audioChannel = new AudioChannel(i,javaCallHelper,codecContext);
        } else if (codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            //视频
            videoChannel = new VideoChannel(i,javaCallHelper,codecContext);
        }

        //音视频模块不存在  单一模块存在可以单独播放流媒体
        if (!audioChannel && !videoChannel) {
            if (javaCallHelper) {
                javaCallHelper->onError(THREAD_CHILD, FFMPEG_NOMEDIA);
            }
            return;
        }

        //准备完成回调java层
        if (javaCallHelper) {
            javaCallHelper->onPrepare(THREAD_CHILD);
        }

    }


}

void PlayControl::start() {
    isPlaying = true;
    if (audioChannel) {
        audioChannel->play();
    }
    if (videoChannel) {
        videoChannel->play();
    }
}


