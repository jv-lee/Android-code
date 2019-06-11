//
// Created by jv.lee on 2019/6/5.
//

#include "AudioChannel.h"

AudioChannel::AudioChannel(int id, JavaCallHelper *javaCallHelper, AVCodecContext *avCodecContext)
        : BaseChannel(id, javaCallHelper, avCodecContext) {

}

void *playThread(void *args) {
    AudioChannel *audioChannel = static_cast<AudioChannel *>(args);
    audioChannel->initOpenSL();
}

void *decodeThread(void *args) {
    AudioChannel *audioChannel = static_cast<AudioChannel *>(args);
//    audioChannel->
}

void AudioChannel::play() {
    //打开队列 设置播放状态
    packet_queue.setWork(1);
    frame_queue.setWork(1);
    isPlaying = true;

    //创建初始化OPENSL ES的线程
    pthread_create(&pid_audio_play, NULL, playThread, this);
    //创建初始化音频解码线程
    pthread_create(&pid_audio_decode, NULL, decodeThread, this);
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
    SLAndroidBufferQueueItf bpPlayerBufferQueue = NULL;

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
    SLDataLocator_AndroidSimpleBufferQueue android_queue = {SL_DATALOCATOR_ANDROIDBUFFERQUEUE};
    SLDataFormat_PCM pcm;
    SLDataSource slDataSource = {&android_queue, &pcm};
    (*engineInterface)->CreateAudioPlayer(engineInterface
            , &bgPlayerObject//播放器
            ,
    );


    //初始化混音器 ------- 4.


    //初始化混音器 ------- 5.

    //初始化混音器 ------- 6.




}

void AudioChannel::stop() {

}




