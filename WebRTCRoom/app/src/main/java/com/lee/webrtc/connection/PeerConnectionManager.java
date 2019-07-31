package com.lee.webrtc.connection;

import org.webrtc.PeerConnection;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class PeerConnectionManager {
    private List<PeerConnection> peerConnections;

    private static final PeerConnectionManager outInstance = new PeerConnectionManager();

    public static PeerConnectionManager getInstance() {
        return outInstance;
    }

    private PeerConnectionManager() {
    }
}
