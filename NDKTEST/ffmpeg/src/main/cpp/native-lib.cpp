//
// Created by jv.lee on 2019-05-25.
//
#include <jni.h>
#include <string>
#include <android/native_window_jni.h>
#include <zconf.h>
#include <android/log.h>

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"lee",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"lee",FORMAT,##__VA_ARGS__);

#define MAX_AUDIO_FRME_SIZE 48000 * 4

extern "C" {
//封装格式
#include <libavformat/avformat.h>
//解码
#include <libavcodec/avcodec.h>
//缩放
#include <libswscale/swscale.h>
//重采样
#include <libswresample/swresample.h>
//图片生成
#include <libavutil/imgutils.h>
}

/**
 * 通过ffmepg绘制视频
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_ffmpeg_MyPlayer_native_1start(JNIEnv *env, jobject instance, jstring path_, jobject surface) {
    const char *path = env->GetStringUTFChars(path_, 0);
    //初始化网络模块
    avformat_network_init();

    //总上下文指针
    AVFormatContext *avFormatContext = avformat_alloc_context();

    //创建一个字典 hashMap
    AVDictionary *opts = NULL;
    //给字典添加键值对  - 字典，键名：超时时间， 值：3秒 flag：
    av_dict_set(&opts, "out_time", "3000000", 0);
    //打开一个视频文件
    int ret = avformat_open_input(&avFormatContext, path, NULL, &opts);
    //判断是否打开成功， 为0则成功 ，不为0则失败
    if (ret) {
        return;
    }

    //解析视频流
    int video_stream_index = -1; //视频流索引
    avformat_find_stream_info(avFormatContext, NULL);
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
        //判断当前流获取的流 解码类型是视频流  获取索引结束循环
        if (avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_stream_index = i;
            break;
        }
    }

    //视频流索引 获取解码参数
    AVCodecParameters *codecPar = avFormatContext->streams[video_stream_index]->codecpar;

    //解码器 h264
    AVCodec *avCodec = avcodec_find_decoder(codecPar->codec_id);
    //解码器上下文
    AVCodecContext *codecContext = avcodec_alloc_context3(avCodec);
    //将解码器参数 copy到解码器上下文
    avcodec_parameters_to_context(codecContext, codecPar);

    //打开一个解码器
    avcodec_open2(codecContext, avCodec, NULL);

    /**
     *   解码 yuv数据 从视频流中获取数据包转换为每一帧 绘制到屏幕
     *   先创建一个空的数据包 AVPacket对象
     *   av_read_frame函数 读取每一帧视频流数据 填入数据包
     *   avcodec_send_packet 将填充好的数据包返回到自身使用
     */
    AVPacket *packet = av_packet_alloc();

    //SWS_FAST_BILINEAR解码时 重视速度，  SWS_BILINEAR 重视质量
    SwsContext *swsContext = sws_getContext(codecContext->width, codecContext->height,
                                            codecContext->pix_fmt, //输入参数 宽高及格式
                                            codecContext->width, codecContext->height, AV_PIX_FMT_RGBA,   //输出参数 宽高及格式
                                            SWS_BILINEAR, 0, 0, 0);

    //初始化需要绘制的window对象 及缓冲区
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    ANativeWindow_Buffer outBuffer;
    //循环内使用的变量 及 对象 必须在外部声明使用
    //创建帧
    AVFrame *frame = av_frame_alloc();
    //接受的容器
    uint8_t *dst_data[0];
    //每一行的首地址
    int dst_linesize[0];
    av_image_alloc(dst_data, dst_linesize, codecContext->width, codecContext->height, AV_PIX_FMT_RGBA, 1);
    //设置缓存区大小 格式宽高
    ANativeWindow_setBuffersGeometry(nativeWindow, codecContext->width, codecContext->height, WINDOW_FORMAT_RGBA_8888);

    while (av_read_frame(avFormatContext, packet) >= 0) {
        avcodec_send_packet(codecContext, packet);

        //将压缩的数据包packet数据 转换为每一帧数据 可以使用帧 绘制到屏幕上展示
        ret = avcodec_receive_frame(codecContext, frame);
        //判断错误数据操作
        if (ret == AVERROR(EAGAIN)) {
            continue;
        } else if (ret < 0) {
            break;
        }

        if (packet->stream_index == video_stream_index) {
            //正在解码
            if (ret == 0) {
                //加锁绘制
                ANativeWindow_lock(nativeWindow, &outBuffer, NULL);
                //绘制  输入源， 输出源
                sws_scale(swsContext, frame->data, frame->linesize, 0, frame->height, dst_data, dst_linesize);
                //渲染
                //拿到一行有多少个字节 RGBA
                int destStride = outBuffer.stride * 4;
                //输入源（rgb）的    AvFrame yum 转换到 image  -> dst_data
                uint8_t *src_data = dst_data[0];
                int src_line_size = dst_linesize[0];
                uint8_t *firstWindow = (uint8_t *)outBuffer.bits;
                for (int i = 0; i < outBuffer.height; ++i) {
                    //内存拷贝进行渲染
                    memcpy(firstWindow + i * destStride, src_data + i * src_line_size, destStride);
                }

                //完成绘制解锁
                ANativeWindow_unlockAndPost(nativeWindow);
                usleep(1000 * 16);
            }
        }
    }

    //释放内存
    av_frame_free(&frame);
    ANativeWindow_release(nativeWindow);
    avcodec_close(codecContext);
    avformat_free_context(avFormatContext);

    env->ReleaseStringUTFChars(path_, path);
}

/**
 * 获取mp3 音频源文件
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_ffmpeg_MyPlayer_sound(JNIEnv *env, jobject instance, jstring input_, jstring output_) {
    const char *input = env->GetStringUTFChars(input_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    avformat_network_init();

    //总上下文
    AVFormatContext *formatContext = avformat_alloc_context();

    //打开音频文件
    if (avformat_open_input(&formatContext, input, NULL, NULL) != 0) {
        LOGI("%s", "无法打开音频文件")
        return;
    }

    //获取输入文件信息
    if (avformat_find_stream_info(formatContext, NULL) < 0) {
        LOGI("%s", "无法获取输入文件信息");
        return;
    }

    //获取音频流 索引
    int audio_stream_index = -1;
    for (int i = 0; i < formatContext->nb_streams; ++i) {
        if (formatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_index = i;
            break;
        }
    }

    AVCodecParameters *codecpar = formatContext->streams[audio_stream_index]->codecpar;

    //解码器
    AVCodec *dec = avcodec_find_decoder(codecpar->codec_id);
    //创建上下文对象
    AVCodecContext *codecContext = avcodec_alloc_context3(dec);
    avcodec_parameters_to_context(codecContext, codecpar);
    avcodec_open2(codecContext, dec, NULL);

    //转换器上下文
    SwrContext *swrContext = swr_alloc();

    //获取输入参数  动态变化的
    AVSampleFormat in_sample = codecContext->sample_fmt;
    int in_sample_rate = codecContext->sample_rate;
    //输入声道布局
    uint64_t in_ch_layout = codecContext->channel_layout;

    //输出参数 固定
    AVSampleFormat out_sample = AV_SAMPLE_FMT_S16;
    //输出采样
    int out_sample_rate = 44100;
    //输出声道布局
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;

    //设置转换器的 输入参数 和输出参数
    swr_alloc_set_opts(swrContext, out_ch_layout, out_sample, out_sample_rate,
                       in_ch_layout, in_sample, in_sample_rate, 0, NULL);
    //初始化转换器其他默认参数
    swr_init(swrContext);
    //创建缓冲区
    uint8_t *out_buffer = (uint8_t *)(av_malloc(2 * 44100));
    //打开文件到输出文件内
    FILE *fp_pcm = fopen(output, "wb");

    //读取包 压缩数据
    AVPacket *packet = av_packet_alloc();
    int count = 0;
    while (av_read_frame(formatContext, packet) >= 0) {
        avcodec_send_packet(codecContext, packet);
        //解压缩数据 未压缩
        AVFrame *frame = av_frame_alloc();
        int ret = avcodec_receive_frame(codecContext, frame);

        //错误判定
        if (ret == AVERROR(EAGAIN)) {
            continue;
        } else if (ret < 0) {
            LOGE("解码完成");
            break;
        }

        //判断当前流是否为音频流
        if (packet->stream_index != audio_stream_index) {
            continue;
        }

        LOGE("正在解码%d",count++);

        //frame -> 转换成统一格式
        swr_convert(swrContext, &out_buffer, 2 * 44100,
                    (const uint8_t **)frame->data, frame->nb_samples);

        int out_channels_nb = av_get_channel_layout_nb_channels(out_ch_layout);
        //缓冲的大小
        int out_buffer_size = av_samples_get_buffer_size(NULL, out_channels_nb, frame->nb_samples, out_sample, 1);
        //最小单位 1 代表字节
        fwrite(out_buffer, 1, out_buffer_size, fp_pcm);
        LOGE("写入文件");
    }

    LOGE("写入结束");

    //关闭上下文 ， 关闭文件
    fclose(fp_pcm);
    av_free(out_buffer);
    swr_free(&swrContext);
    avcodec_close(codecContext);
    avformat_close_input(&formatContext);


    env->ReleaseStringUTFChars(input_, input);
    env->ReleaseStringUTFChars(output_, output);
}