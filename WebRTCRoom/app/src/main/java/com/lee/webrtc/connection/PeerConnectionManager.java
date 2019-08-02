package com.lee.webrtc.connection;

import android.content.Context;

import com.alibaba.fastjson.asm.Label;
import com.lee.webrtc.ChatRoomActivity;
import com.lee.webrtc.socket.JavaWebSocket;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class PeerConnectionManager {

    private ChatRoomActivity mContext;
    private String myId;
    /**
     * 是否开启视频画面
     */
    private boolean videoEnable;

    /**
     * 线程池
     */
    private ExecutorService executor;

    /**
     * p2p 连接
     */
    private List<PeerConnection> peerConnections;

    /**
     * PeerConnection初始化工厂 构建 音视频会话管理器
     */
    private PeerConnectionFactory factory;

    /**
     * OpenGl上下文
     */
    private EglBase eglBase;

    private static final String STREAM_LABEL = "ARDAMS";

    /**
     * 多媒体音视频流
     */
    private MediaStream localStream;

    /**
     * 音频源
     */
    private AudioSource audioSource;

    /**
     * 音频轨道
     */
    private AudioTrack localAudioTrack;

    /**
     * 视频源
     */
    private VideoSource videoSource;

    /**
     * 视频轨道
     */
    private VideoTrack localVideoTrack;

    /**
     * 回音消除标识符
     */
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    /**
     * 噪音抑制标识符
     */
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    /**
     * 自动增益控制
     */
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    /**
     * 高通滤波器
     */
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";

    /**
     * 获取摄像头的设备 camera1 camera2
     */
    private VideoCapturer capturerAndroid;


    /**
     * 帮助渲染到本地预览
     */
    private SurfaceTextureHelper surfaceTextureHelper;


    private static final PeerConnectionManager outInstance = new PeerConnectionManager();

    public static PeerConnectionManager getInstance() {
        return outInstance;
    }

    private PeerConnectionManager() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void initContext(ChatRoomActivity context, EglBase eglBase) {
        this.eglBase = eglBase;
        this.mContext = context;
    }

    /**
     * 连接至音视频会话房间
     *
     * @param javaWebSocket socket连接
     * @param isVideoEnable 是否开启视频
     * @param connections   连接数据
     * @param myId          自身id
     */
    public void joinToRoom(JavaWebSocket javaWebSocket, boolean isVideoEnable, ArrayList<String> connections, String myId) {
        this.videoEnable = isVideoEnable;
        this.myId = myId;
        //大量的初始化 PeerConnection
        executor.execute(() -> {
            if (factory == null) {
                factory = createConnectionFactory();
            }

            if (localStream == null) {
                createLocalStream();
            }
        });
    }

    private void createLocalStream() {
        localStream = factory.createLocalMediaStream(STREAM_LABEL);

        //音频
        audioSource = factory.createAudioSource(createAudioConstraints());
        //采集音频
        localAudioTrack = factory.createAudioTrack(STREAM_LABEL + "a0", audioSource);
        //增加音频轨道
        localStream.addTrack(localAudioTrack);

        //是否开启视频源
        if (videoEnable) {
            //视频源
            capturerAndroid = createVideoCapture();
            videoSource = factory.createVideoSource(capturerAndroid.isScreencast());

            surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.getEglBaseContext());
            //初始化 capturerAndroid
            capturerAndroid.initialize(surfaceTextureHelper, mContext, videoSource.getCapturerObserver());
            //摄像头开始预览:  宽度，高度，帧率
            capturerAndroid.startCapture(320, 240, 10);
            //视频轨道
            localVideoTrack = factory.createVideoTrack(STREAM_LABEL + "v0", videoSource);
            //视屏轨道添加到多媒体流
            localStream.addTrack(localVideoTrack);
            if (mContext != null) {
                mContext.setLocalStream(localStream, myId);
            }
        }
    }

    /**
     * 创建摄像头对象
     *
     * @return
     */
    private VideoCapturer createVideoCapture() {
        VideoCapturer videoCapturer = null;
        if (Camera2Enumerator.isSupported(mContext)) {
            Camera2Enumerator enumerator = new Camera2Enumerator(mContext);
            videoCapturer = createCameraCapture(enumerator);
        } else {
            Camera1Enumerator enumerator = new Camera1Enumerator(true);
            videoCapturer = createCameraCapture(enumerator);
        }
        return videoCapturer;
    }

    /**
     * 通过api获取前置摄像头capture
     *
     * @return
     */
    private VideoCapturer createCameraCapture(CameraEnumerator enumerator) {
        String[] deviceNames = enumerator.getDeviceNames();
        for (String deviceName : deviceNames) {
            //前置摄像头
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
            //后置摄像头
            if (enumerator.isBackFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }

    private MediaConstraints createAudioConstraints() {
        MediaConstraints audioConstraints = new MediaConstraints();
        //设置回音消除
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT, "true"));
        //设置噪音抑制
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT, "true"));
        //不设置自动增益
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT, "false"));
        //不设置高通滤波器
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT, "false"));
        return audioConstraints;
    }

    private PeerConnectionFactory createConnectionFactory() {
        //初始化参数设置为默认
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(mContext).createInitializationOptions());
        /**
         * options 参数
         *         static final int ADAPTER_TYPE_UNKNOWN = 0; //未知
         *         static final int ADAPTER_TYPE_ETHERNET = 1; // 以太网
         *         static final int ADAPTER_TYPE_WIFI = 2; // WIFI
         *         static final int ADAPTER_TYPE_CELLULAR = 4; // 移动网络
         *         static final int ADAPTER_TYPE_VPN = 8; // vpn
         *         static final int ADAPTER_TYPE_LOOPBACK = 16; // 回环
         *         static final int ADAPTER_TYPE_ANY = 32;
         *
         *         public int networkIgnor eMask; //网络忽略
         *         public boolean disableEncryption; // 解码器
         *         public boolean disableNetworkMonitor; //网络监控
         *         入参出参
         */
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        /**
         * 初始化 视频解码器 / 编码器
         */
        DefaultVideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(eglBase.getEglBaseContext(), true, true);
        DefaultVideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());

        return PeerConnectionFactory.builder()
                .setOptions(options)
                .setAudioDeviceModule(JavaAudioDeviceModule.builder(mContext).createAudioDeviceModule())
                .setVideoDecoderFactory(decoderFactory)
                .setVideoEncoderFactory(encoderFactory)
                .createPeerConnectionFactory();
    }

}
