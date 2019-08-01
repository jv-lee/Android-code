package com.lee.webrtc.connection;

import android.content.Context;

import com.lee.webrtc.socket.JavaWebSocket;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
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

    private Context mContext;
    /**
     * 是否开启视频画面
     */
    private boolean isVideoEnable;

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

    private EglBase eglBase;

    private static final PeerConnectionManager outInstance = new PeerConnectionManager();

    public static PeerConnectionManager getInstance() {
        return outInstance;
    }

    private PeerConnectionManager() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void initContext(Context context, EglBase eglBase) {
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
        this.isVideoEnable = isVideoEnable;
        //大量的初始化 PeerConnection
        executor.execute(() -> {
            if (factory == null) {
                factory = createConnectionFactory();
            }
        });
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
