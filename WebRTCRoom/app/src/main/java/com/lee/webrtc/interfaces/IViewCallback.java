package com.lee.webrtc.interfaces;

import org.webrtc.MediaStream;

/**
 * @author jv.lee
 * @date 2019-09-08
 * @description
 */
public interface IViewCallback {
    void onSetLocalStream(MediaStream stream, String socketId);

    void onAddRemoteStream(MediaStream stream, String socketId);

    void onCloseWithId(String socketId);
}
