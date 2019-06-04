package com.lee.code.livepush.meida;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import com.lee.code.livepush.LivePusher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 */
public class AudioChannel {
    private LivePusher mLivePusher;
    private AudioRecord mAudioRecord;
    private int inputSamples;
    private int channels = 2;
    private int channelConfig;
    private int minBufferSize;
    private ExecutorService executor;
    private boolean isLiveing;


    public AudioChannel(LivePusher livePusher) {
        executor = Executors.newSingleThreadExecutor();
        mLivePusher = livePusher;
        if (channelConfig == 2) {
            //双声道
            channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        } else {
            channelConfig = AudioFormat.CHANNEL_IN_MONO;
        }
        //初始化编码器参数
        mLivePusher.nativeSetAudioEncInfo(44100, channels);
        //获取native层编码器返回的缓冲区大小
        inputSamples = mLivePusher.getInputSamples() * 2;
        //乘以2 双声道
        minBufferSize = AudioRecord.getMinBufferSize(44100, channelConfig, AudioFormat.ENCODING_PCM_16BIT) * 2;
        //音频来源 MIC麦克风、采样频率 44100 ， channelConfig 默认给双通道 , 采样位数 16位 ， 采样数据缓冲区大小(判断系统返回的缓冲大小 大于实际native层的大小 使用实际大小 否者使用系统返回的)
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, channelConfig, AudioFormat.ENCODING_PCM_16BIT, minBufferSize > inputSamples ? inputSamples : minBufferSize);
    }

    public void startLive() {
        isLiveing = true;
        executor.submit(new AudioTeask());
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    class AudioTeask implements Runnable {

        @Override
        public void run() {
            //开始录制读取声音
            mAudioRecord.startRecording();
            byte[] bytes = new byte[inputSamples];
            while (isLiveing) {
                mAudioRecord.read(bytes, 0, bytes.length);
                mLivePusher.nativePushAudio(bytes);
            }
        }
    }

}
