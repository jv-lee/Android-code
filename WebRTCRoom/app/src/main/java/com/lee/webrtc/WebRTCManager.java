package com.lee.webrtc;

import android.app.Activity;

import com.lee.webrtc.connection.PeerConnectionManager;
import com.lee.webrtc.interfaces.IViewCallback;
import com.lee.webrtc.permission.PermissionRequest;
import com.lee.webrtc.socket.JavaWebSocket;

import org.webrtc.EglBase;

/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class WebRTCManager {

    private JavaWebSocket webSocket;
    private PeerConnectionManager peerConnectionManager;
    private String roomId = "";

    private static final WebRTCManager ourInstance = new WebRTCManager();

    public static WebRTCManager getInstance() {
        return ourInstance;
    }

    private WebRTCManager() {
    }

    /**
     * 初始化连接 创建socket连接， 音视频会话管理器
     *
     * @param activity
     * @param roomId
     */
    public void connect(MainActivity activity, String roomId) {
        this.roomId = roomId;
        webSocket = new JavaWebSocket(activity);
        peerConnectionManager = PeerConnectionManager.getInstance();
        webSocket.connect("wss://47.107.132.117/wss");
    }

    /**
     * 开启连接至房间
     *
     * @param chatRoomActivity
     * @param eglBase
     */
    public void joinRoom(ChatRoomActivity chatRoomActivity, EglBase eglBase) {
        peerConnectionManager.initContext(chatRoomActivity, eglBase);
        webSocket.joinRoom(roomId);
    }

    public void toggle(boolean enableMic) {
        if (peerConnectionManager != null) {
            peerConnectionManager.toggleSpeaker(enableMic);
        }
    }

    public void toggleLarge(boolean isChecked) {
        if (peerConnectionManager != null) {
            peerConnectionManager.toggleLarge(isChecked);
        }
    }

    public void toggleCameraEnable(boolean isChecked) {
        if (peerConnectionManager != null) {
            peerConnectionManager.toggleCameraEnable(isChecked);
        }
    }

    public void toggleCameraDevice(boolean isChecked) {
        if (peerConnectionManager != null) {
            peerConnectionManager.toggleCameraDevice(isChecked);
        }
    }

    public void exitRoom() {
        if (peerConnectionManager != null) {
            webSocket = null;
            peerConnectionManager.exitRoom();
        }
    }

    public void setViewCallback(IViewCallback viewCallback) {
        if (peerConnectionManager != null) {
            peerConnectionManager.setViewCallback(viewCallback);
        }
    }
}
