package com.lee.webrtc;

import android.app.Activity;

import com.lee.webrtc.connection.PeerConnectionManager;
import com.lee.webrtc.permission.PermissionRequest;
import com.lee.webrtc.socket.JavaWebSocket;

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

    public void connect(MainActivity activity, String roomId) {
        this.roomId = roomId;
        webSocket = new JavaWebSocket(activity);
        peerConnectionManager = PeerConnectionManager.getInstance();
        webSocket.connect("wss://47.107.132.117/wss");
    }

    public void joinRoom(ChatRoomActivity chatRoomActivity) {
        webSocket.joinRoom(roomId);
    }
}
