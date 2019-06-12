//
// Created by jv.lee on 2019/6/5.
//



#include "AudioChannel.h"

AudioChannel::AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext,AVRational time_base)
        : BaseChannel(id, javaCallHelper, avCodecContext,time_base) {
//根据布局获取声道数
    out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
    out_samplesize = av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);
    out_sample_rate = 44100;
    //CD标准
    //441000 双声道 2字节  out_samplesize 16位 2个字节  out_channels 2
    buffer = (uint8_t *)malloc(out_sample_rate * out_samplesize * out_channels);
}

void *playThread(void *args) {
    AudioChannel *audioChannel = static_cast<AudioChannel *>(args);
    audioChannel->initOpenSL();
    return 0;
}

void *decodeThread(void *args) {
    AudioChannel *audioChannel = static_cast<AudioChannel *>(args);
    audioChannel->decode();
    return 0;
}

void AudioChannel::play() {
    //初始化转换器上下文
    swr_ctx = swr_alloc_set_opts(0, AV_CH_LAYOUT_STEREO, AV_SAMPLE_FMT_S16, out_sample_rate,
                                 avCodecContext->channel_layout,
                                 avCodecContext->sample_fmt,
                                 avCodecContext->sample_rate, 0, 0);
    swr_init(swr_ctx);

    //打开队列 设置播放状态
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;

    //创建初始化OPENSL ES的线程
    pthread_create(&pid_audio_play, NULL, playThread, this);
    //创建初始化音频解码线程
    pthread_create(&pid_audio_decode, NULL, decodeThread, this);
}

void AudioChannel::stop() {

}

void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    //
    AudioChannel *audioChannel = static_cast<AudioChannel *>(context);
    //pcm 原始音频数据
    int datalen = audioChannel->getPcm();
    if (datalen > 0) {
        (*bq)->Enqueue(bq, audioChannel->buffer, datalen);
    }
}

void AudioChannel::initOpenSL() {
    //音频引擎
    SLEngineItf engineInterface = NULL;
    //音频对象（音频引擎参数）
    SLObjectItf engineObject = NULL;

    //混音器
    SLObjectItf outputMixObject = NULL;

    //播放器
    SLObjectItf bpPlayerObject = NULL;
    //回调接口
    SLPlayItf bpPlayerInterface = NULL;
    //缓冲队列
    SLAndroidSimpleBufferQueueItf bpPlayerBufferQueue = NULL;

    //初始化播放引擎 ------- 1.
    SLresult result;
    result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
    if (SL_RESULT_SUCCESS != result) {
        return;
    }

    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        return;
    }

    //音频接口 (类似SurfaceHolder)
    result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineInterface);
    if (SL_RESULT_SUCCESS != result) {
        return;
    }

    //初始化混音器 ------- 2.
    result = (*engineInterface)->CreateOutputMix(engineInterface, &outputMixObject, 0, 0, 0);
    if (SL_RESULT_SUCCESS != result) {
        return;
    }
    result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        return;
    }

    //创建播放器 ------- 3.
    SLDataLocator_AndroidSimpleBufferQueue android_queue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,2};
    //pcm数据格式
    SLDataFormat_PCM pcm = {SL_DATAFORMAT_PCM,//播放pcm格式的数据
                            2,//双声道（立体声）
                            SL_SAMPLINGRATE_44_1,//44100hz的频率
                            SL_PCMSAMPLEFORMAT_FIXED_16,//位数 16
                            SL_PCMSAMPLEFORMAT_FIXED_16,
                            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,//立体声前左前右
                            SL_BYTEORDER_LITTLEENDIAN};//小端模式

    SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
    SLDataSink audioSnk = {&outputMix, NULL};

    SLDataSource slDataSource = {&android_queue, &pcm};

    const SLInterfaceID ids[1] = {SL_IID_BUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};
    (*engineInterface)->CreateAudioPlayer(engineInterface,
                                          &bpPlayerObject,//播放器
                                          &slDataSource,//播放器参数 播放缓冲队列 播放格式
                                          &audioSnk,//播放缓冲区
                                          1,//播放接口回调个数
                                          ids,//设置播放器队列ID
                                          req);

    //初始化播放器 ------- 4.
    (*bpPlayerObject)->Realize(bpPlayerObject, SL_BOOLEAN_FALSE);
    //得到接口后调用 获取Player接口
    (*bpPlayerObject)->GetInterface(bpPlayerObject, SL_IID_PLAY, &bpPlayerInterface);
    //获得播放器接口
    (*bpPlayerObject)->GetInterface(bpPlayerObject, SL_IID_BUFFERQUEUE, &bpPlayerBufferQueue);

    (*bpPlayerBufferQueue)->RegisterCallback(bpPlayerBufferQueue, bqPlayerCallback, this);
    //设置播放状态
    (*bpPlayerInterface)->SetPlayState(bpPlayerInterface, SL_PLAYSTATE_PLAYING);
    bqPlayerCallback(bpPlayerBufferQueue, this);
    LOGE("--- 手动调用播放 packet:%d", this->packet_queue.size());

    //初始化混音器 ------- 5.

    //初始化混音器 ------- 6.




}

/**
 * 音频pcm数据 解码
 */
void AudioChannel::decode() {
    AVPacket *packet = 0;
    while (isPlaying) {
        //音频packet
        int result = packet_queue.deQueue(packet);
        if (!isPlaying) {
            break;
        }
        if (!result) {
            continue;
        }

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
        if (result == AVERROR(EAGAIN)) {
            //需要更多数据
            continue;
        } else if (result < 0) {
            break;
        }

        while (frame_queue.size() > 100 && isPlaying) {
            av_usleep(1000 * 10);
            continue;
        }
        frame_queue.enQueue(frame);

    }
}

int AudioChannel::getPcm() {
    AVFrame *frame = 0;
    int data_size = 0;
    int result;
    while (isPlaying) {
        result = frame_queue.deQueue(frame);
        if (!isPlaying) {
            break;
        }
        if (!result) {
            continue;
        }
        uint64_t dst_nb_samples = av_rescale_rnd(
                swr_get_delay(swr_ctx, frame->sample_rate) + frame->nb_samples, out_sample_rate,
                frame->sample_rate, AV_ROUND_UP);
        //转换，返回值为转换后的sample个数
        int nb = swr_convert(swr_ctx, &buffer, dst_nb_samples, (const uint8_t **) frame->data,
                             frame->nb_samples);
        //转换后多少数据 buffer size
        data_size = nb*out_channels*out_samplesize;

        //计算时间线
        clock = frame->pts * av_q2d(time_base);
        break;
    }
    releaseAvFrame(frame);
    return data_size;
}






