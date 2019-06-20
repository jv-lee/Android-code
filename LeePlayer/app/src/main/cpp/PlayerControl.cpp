//
// Created by jv.lee on 2019/6/5.
//

#include "PlayerControl.h"
#include "JavaCallHelper.h"
#include "macro.h"

PlayControl::PlayControl(JavaCallHelper *javaCallHelper, const char *dataSource) {
    url = new char[strlen(dataSource) + 1];
    this->javaCallHelper = javaCallHelper;
    //因为在上一层已经释放了， 所以在控制层再次拷贝url数据
    strcpy(url, dataSource);
    duration = 0;
    //初始化线程锁
    pthread_mutex_init(&seekMutex, 0);
}

PlayControl::~PlayControl() {
    pthread_mutex_destroy(&seekMutex);
    javaCallHelper = 0;
    DELETE(url);
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

    //获取进度条
    duration = formatContext->duration / 1000000;

    for (int i = 0; i < formatContext->nb_streams; ++i) {
        AVCodecParameters *codecpar = formatContext->streams[i]->codecpar;
        //获取流 音视频同步处理
        AVStream *stream = formatContext->streams[i];
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
            audioChannel = new AudioChannel(i, javaCallHelper, codecContext, stream->time_base);
        } else if (codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            //获取视频帧率
            AVRational frame_rate = stream->avg_frame_rate;
//            int fps = frame_rate.num / frame_rate.den;
            int fps = static_cast<int>(av_q2d(frame_rate));

            //视频
            videoChannel = new VideoChannel(i, javaCallHelper, codecContext, stream->time_base);
            videoChannel->setRenderCallback(renderFrame);
            videoChannel->setFps(fps);
        }
    }

    //音视频模块不存在  单一模块存在可以单独播放流媒体
    if (!audioChannel && !videoChannel) {
        if (javaCallHelper) {
            javaCallHelper->onError(THREAD_CHILD, FFMPEG_NOMEDIA);
        }
        return;
    }

    //将audioChannel 赋值给videoChannel 进行音视频同步
    videoChannel->audioChannel = audioChannel;

    //准备完成回调java层
    if (javaCallHelper) {
        javaCallHelper->onPrepare(THREAD_CHILD);
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
    if (audioChannel) {
        audioChannel->play();
    }
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
                audioChannel->packet_queue.enQueue(packet);
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

int PlayControl::getDuration() {
    return this->duration;
}

void PlayControl::seek(int progress) {
    //进去必须在 0-duration之间
    if (progress < 0 || progress >= duration) {
        return;
    }
    if (!formatContext) {
        return;
    }
    //线程加锁
    pthread_mutex_lock(&seekMutex);

    isSeek = 1;
    //换算百分比为微秒
    int64_t seek = progress * 1000000;
    LOGE("时间%d", seek);

    //设置seek位置
    av_seek_frame(formatContext, -1, seek, AVSEEK_FLAG_BACKWARD);
    //清空队列
    if (audioChannel) {
        audioChannel->stopWork();
        audioChannel->clear();
        audioChannel->startWork();
    }
    if (videoChannel) {
        videoChannel->stopWork();
        videoChannel->clear();
        videoChannel->startWork();
    }
    pthread_mutex_unlock(&seekMutex);
    isSeek = 0;
}

void *async_stop(void *args) {
    PlayControl *playControl = static_cast<PlayControl *>(args);
    //关闭解码线程和播放线程
    pthread_join(playControl->pid_prepare, 0);
    pthread_join(playControl->pid_start, 0);
    playControl->isPlaying = 0;
    DELETE(playControl->audioChannel);
    DELETE(playControl->videoChannel);
    if (playControl->formatContext) {
        avformat_close_input(&playControl->formatContext);
        avformat_free_context(playControl->formatContext);
        playControl->formatContext = NULL;
    }
    DELETE(playControl);
    LOGE("释放....");
    return 0;
}

void PlayControl::stop() {
    javaCallHelper = 0;
    if (audioChannel) {
        audioChannel->javaCallHelper = 0;
    }
    if (videoChannel) {
        videoChannel->javaCallHelper = 0;
    }
    //停止解码线程while循环
    isPlaying = 0;
    //创建释放线程
    pthread_create(&pid_stop, 0, async_stop, this);
}
