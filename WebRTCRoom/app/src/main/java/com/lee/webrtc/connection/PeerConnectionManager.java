package com.lee.webrtc.connection;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.lee.webrtc.ChatRoomActivity;
import com.lee.webrtc.interfaces.IViewCallback;
import com.lee.webrtc.socket.JavaWebSocket;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class PeerConnectionManager {

    private static final String TAG = "lee>>>";

    private Context mContext;
    private IViewCallback viewCallback;
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

    /**
     * ICE服务器的集合
     */
    private ArrayList<PeerConnection.IceServer> iceServers;

    /**
     * 会议室所有用户ID
     */
    private ArrayList<String> connectionIdArray;

    /**
     * 会议室的每一个用户，会对本地实现一个p2p连接(PeerConnection)
     */
    private Map<String, Peer> connectionPeerDic;


    /**
     * 当前用户角色
     */
    private Role role;

    private JavaWebSocket javaWebSocket;

    /**
     * 声音服务类
     */
    private AudioManager audioManager;

    public void setViewCallback(IViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    /**
     * 角色枚举
     */
    enum Role {
        /**
         * 邀请者 (第一次进入会议室童话)
         */
        Caller,
        /**
         * 接受者(进入别人的会议室)
         */
        Receiver}

    /**
     * 切换是否允许将本地的麦克风数据推送到远端
     *
     * @param enableMic
     */
    public void toggleSpeaker(boolean enableMic) {
        if (localAudioTrack != null) {

            localAudioTrack.setEnabled(enableMic);
        }
    }

    /**
     * 切换播放设备，免提模式
     *
     * @param isChecked
     */
    public void toggleLarge(boolean isChecked) {
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(isChecked);
        }
    }

    /**
     * 关闭摄像头预览
     *
     * @param isChecked
     */
    public void toggleCameraEnable(boolean isChecked) {
        if (localVideoTrack != null) {
            localVideoTrack.setEnabled(isChecked);
        }
    }

    /**
     * 切换前置后置摄像头
     *
     * @param isChecked
     */
    public void toggleCameraDevice(boolean isChecked) {
        if (capturerAndroid != null && capturerAndroid instanceof CameraVideoCapturer) {
            CameraVideoCapturer cameraVideoCapturer = (CameraVideoCapturer) capturerAndroid;
            cameraVideoCapturer.switchCamera(null);
        }
    }


    /**
     * 耗时操作
     */
    public void exitRoom() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> myCopy = (ArrayList<String>) connectionIdArray.clone();
                for (String id : myCopy) {
                    closePeerConnection(id);
                }
                //清空单列集合
                if (connectionIdArray != null) {
                    connectionIdArray.clear();
                }
                //关闭音频推流
                if (audioSource != null) {
                    audioSource.dispose();
                    audioSource = null;
                }
                //关闭视频推流
                if (videoSource != null) {
                    videoSource.dispose();
                    videoSource = null;
                }
                //关闭摄像头预览
                if (capturerAndroid != null) {
                    try {
                        capturerAndroid.stopCapture();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //关闭辅助类
                if (surfaceTextureHelper != null) {
                    surfaceTextureHelper.dispose();
                    surfaceTextureHelper = null;
                }
                //关闭工厂
                if (factory != null) {
                    factory.dispose();
                    factory = null;
                }
            }
        });
    }

    private void closePeerConnection(String id) {
        //拿到链接的封装对象
        Peer mPeer = connectionPeerDic.get(id);
        if (mPeer != null) {
            //关闭了P2P连接
            mPeer.peerConnection.close();
        }
        connectionPeerDic.remove(id);
        connectionIdArray.remove(id);
        if (viewCallback != null) {
            viewCallback.onCloseWithId(id);
        }
    }

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
        this.iceServers = new ArrayList<>();
        this.connectionPeerDic = new HashMap<>();
        this.connectionIdArray = new ArrayList<>();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        PeerConnection.IceServer iceServer1 = PeerConnection.IceServer
                .builder("stun:47.107.132.117:3478?transport=udp")
                .setUsername("")
                .setPassword("")
                .createIceServer();

        PeerConnection.IceServer iceServer2 = PeerConnection.IceServer
                .builder("turn:47.107.132.117:3478?transport=udp")
                .setUsername("ddssingsong")
                .setPassword("123456")
                .createIceServer();
        iceServers.add(iceServer1);
        iceServers.add(iceServer2);

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
        this.javaWebSocket = javaWebSocket;
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
            //添加聊天室所有id
            connectionIdArray.addAll(connections);
            createPeerConnections();
            //本地的数据流推向会议室的每一个人(具备连接交互的能力)
            addStream();
            //发送邀请
            createOffers();
        });
    }

    /**
     * 为所有的连接创建offer 邀请
     */
    private void createOffers() {
        for (Map.Entry<String, Peer> entry : connectionPeerDic.entrySet()) {
            //赋值角色
            role = Role.Caller;
            Peer peer = entry.getValue();
            //每一位会议室的人发送邀请，并且传递我的数据类型（音频/视频） 内部网络请求
            peer.peerConnection.createOffer(peer, offerOrAnswerConstraint());
        }
    }

    /**
     * 媒体约束
     * 设置传输音视频
     *
     * @return
     */
    private MediaConstraints offerOrAnswerConstraint() {
        MediaConstraints mediaConstraints = new MediaConstraints();
        ArrayList<MediaConstraints.KeyValuePair> keyValuePairs = new ArrayList<>();
        //是否传输音频
        keyValuePairs.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "ture"));
        //是否传输视频
        keyValuePairs.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", String.valueOf(videoEnable)));
        mediaConstraints.mandatory.addAll(keyValuePairs);
        return mediaConstraints;
    }

    private void addStream() {
        Log.v(TAG, "为所有连接添加流");
        for (Map.Entry<String, Peer> entry : connectionPeerDic.entrySet()) {
            if (localStream == null) {
                createLocalStream();
            }
            entry.getValue().peerConnection.addStream(localStream);
        }
    }

    /**
     * 创建音视频会话连接
     * 建立会议室每一个用户的连接
     */
    private void createPeerConnections() {
        for (String id : connectionIdArray) {
            Peer peer = new Peer(id);
            connectionPeerDic.put(id, peer);
        }
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
            if (viewCallback != null) {
                viewCallback.onSetLocalStream(localStream,myId);
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

    /**
     * 当别人在会议室 ，  我再进去
     *
     * @param socketId
     * @param iceCandidate
     */
    public void onRemoteIceCandidate(String socketId, IceCandidate iceCandidate) {
        //通过socketId 取出连接对象
        Peer peer = connectionPeerDic.get(socketId);
        if (peer != null) {
            peer.peerConnection.addIceCandidate(iceCandidate);
        }
    }

    public void onReceiverAnswer(String socketId, String sdp) {
        //对方的对话 sdp
        executor.execute(() -> {
            Peer peer = connectionPeerDic.get(socketId);
            SessionDescription sessionDescription = new SessionDescription(SessionDescription.Type.ANSWER, sdp);
            if (peer != null) {
                peer.peerConnection.setRemoteDescription(peer, sessionDescription);
            }
        });
    }

    private class Peer implements SdpObserver, PeerConnection.Observer {
        /**
         * my id 跟远端用户之间的连接
         */
        private PeerConnection peerConnection;

        /**
         * socket是其他用的id
         */
        private String socketId;

        public Peer(String socketId) {
            this.socketId = socketId;
            PeerConnection.RTCConfiguration rtcConfiguration = new PeerConnection.RTCConfiguration(iceServers);
            peerConnection = factory.createPeerConnection(rtcConfiguration, this);
        }

        /**
         * 内网状态发生改变，音视频童话中 4G -> 切换成wifi
         *
         * @param signalingState
         */
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

        }

        /**
         * 连接上了ICE服务器/断开连接
         *
         * @param iceConnectionState
         */
        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        }


        @Override
        public void onIceConnectionReceivingChange(boolean b) {

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

        }

        /**
         * 调用的时机有两次
         * 第一次在连接到ICE服务器的时候 调用次数是网络中有多少个路由节点（1-n）
         * 第二次（有人进入这个房间） 对方 到ICE服务器的路由节点，调用次数是 视频童话的人在网络中离ICE服务器有多少个路由器节点（1-n）
         *
         * @param iceCandidate
         */
        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            //socket -> 传递
            javaWebSocket.sendIceCandidate(socketId, iceCandidate);
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

        }

        /**
         * p2p建立成功 mediaStream（音视频流）
         *
         * @param mediaStream
         */
        @Override
        public void onAddStream(MediaStream mediaStream) {
            if (viewCallback != null) {
                viewCallback.onAddRemoteStream(mediaStream,socketId);
            }
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {

        }

        @Override
        public void onRenegotiationNeeded() {

        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

        }

        // *************** SdpObserver ******************//

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.v(TAG, "onCreateSuccess:" + sessionDescription.description);
            //设置本地的SDP 如果设置成功则回调 onSetSuccess
            peerConnection.setLocalDescription(this, sessionDescription);
        }

        @Override
        public void onSetSuccess() {
            Log.v(TAG, "onSetSuccess");
            //交换彼此的sdp ， iceCandidate
            //当前状态为本地主动发起交换
            if (peerConnection.signalingState() == PeerConnection.SignalingState.HAVE_LOCAL_OFFER) {
                javaWebSocket.sendOffer(socketId, peerConnection.getLocalDescription());
            }
        }

        @Override
        public void onCreateFailure(String s) {
            Log.v(TAG, "onCreateFailure:" + s);
        }

        @Override
        public void onSetFailure(String s) {
            Log.v(TAG, "onSetFailure:" + s);
        }
    }

}
