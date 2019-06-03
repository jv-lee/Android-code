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
    private int channels = 2;
    private int channelConfig;
    private int minBufferSize;
    private ExecutorService executor;
    private boolean isLiveing;


    public AudioChannel(LivePusher livePusher) {
        executor = Executors.newSingleThreadExecutor();
        mLivePusher = livePusher;
        if (channelConfig == 2) {
            channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        }else{
            channelConfig = AudioFormat.CHANNEL_IN_MONO;
        }
        minBufferSize = AudioRecord.getMinBufferSize(44100, channelConfig, AudioFormat.ENCODING_PCM_16BIT) * 2;
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, channelConfig, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
    }

    public void startlLive(){
        isLiveing = true;
//        executor.submit(new AudioTeask());
    }

    public void setChannels(int channels) {

    }


}
