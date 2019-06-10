//
// Created by jv.lee on 2019/6/5.
//

#include "PlayerControl.h"
#include "macro.h"

PlayControl::PlayControl(JavaCallHelper *javaCallHelper, const char *dataSource) {
    url = new char[strlen(dataSource) + 1];
    this->javaCallHelper = javaCallHelper;
    //因为在上一层已经释放了， 所以在控制层再次拷贝url数据
    strcpy(url, dataSource);
}

PlayControl::~PlayControl() {

}

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
    //子线程访问到对象的属性
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
            audioChannel = new AudioChannel(i, javaCallHelper, codecContext);
        } else if (codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            //视频
            videoChannel = new VideoChannel(i, javaCallHelper, codecContext);
            videoChannel->setRenderCallback(renderFrame);
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

/**
 * 开始解码线程
 * @param args
 * @return
 */
void *startThread(void *args) {
    PlayControl *playControl = static_cast<PlayControl *>(args);
    playControl->startControl();
    return 0;
}

void PlayControl::start() {
    isPlaying = true;
//    if (audioChannel) {
//        audioChannel->play();
//    }
    if (videoChannel) {
        videoChannel->play();
    }
    pthread_create(&pid_start, NULL, startThread, this);
}

void PlayControl::startControl() {
    int result = 0;
    while (isPlaying) {
        //读取文件packet速度快于渲染 frame ， 渲染需要线程等待  100帧
        if (audioChannel && audioChannel->packet_queue.size() > 100) {
            //生产者的速度大于消费者的速度， 休眠10ms 毫秒
            av_usleep(1000 * 10);
            continue;
        }
        if (videoChannel && videoChannel->packet_queue.size() > 100) {
            av_usleep(1000 * 10);
            continue;
        }

        //读取数据包
        AVPacket *packet = av_packet_alloc();
        result = av_read_frame(formatContext, packet);
        if (result == 0) {
            //将数据包加入队列
            if (audioChannel && packet->stream_index == audioChannel->channelID) {
//                audioChannel->packet_queue.enQueue(packet);
            } else if (videoChannel && packet->stream_index == videoChannel->channelID) {
                videoChannel->packet_queue.enQueue(packet);
            }
        } else if (result == AVERROR_EOF) {
            //读取到文件末尾 读取完毕不一定播放完毕
            if (videoChannel->packet_queue.empty() && videoChannel->frame_queue.empty() &&
                audioChannel->packet_queue.empty() && audioChannel->frame_queue.empty()) {
                LOGE("播放完毕 ...");
                break;
            }
        } else {
            //因为seek的存在，就算读取完毕依然要循环去执行 av_read_frame(否则seek了没用)
            break;
        }
    }

    //播放完毕修改状态
    isPlaying = 0;
    audioChannel->stop();
    videoChannel->stop();

}

void PlayControl::setRenderCallback(RenderFrame renderFrame) {
    this->renderFrame = renderFrame;
}


