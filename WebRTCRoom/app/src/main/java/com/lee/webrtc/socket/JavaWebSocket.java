package com.lee.webrtc.socket;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lee.webrtc.ChatRoomActivity;
import com.lee.webrtc.MainActivity;
import com.lee.webrtc.connection.PeerConnectionManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author jv.lee
 * @date 2019-07-31
 * @description
 */
public class JavaWebSocket {
    private static final String TAG = "JavaWebSocket";
    private WebSocketClient mWebSocketClient;
    private PeerConnectionManager peerConnectionManager;
    private MainActivity mActivity;

    public JavaWebSocket(MainActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 和服务器建立socket连接
     * @param wss
     */
    public void connect(String wss) {
        //初始化peer音视频会话房间管理器
        peerConnectionManager = PeerConnectionManager.getInstance();

        URI uri = null;
        try {
            uri = new URI(wss);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //创建socket连接
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i(TAG, "onOpen");
                ChatRoomActivity.openActivity(mActivity);
            }

            @Override
            public void onMessage(String message) {
                Log.i(TAG, "onMessage:" + message);
                handlerMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onError(Exception ex) {
                Log.i(TAG, "onError");
            }
        };
        if (wss.startsWith("wss")) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrustManagerTest()}, new SecureRandom());
                SSLSocketFactory factory = null;
                if (sslContext != null) {
                    factory = sslContext.getSocketFactory();
                }
                if (factory != null) {
                    mWebSocketClient.setSocket(factory.createSocket());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mWebSocketClient.connect();
    }

    /**
     * 发送消息进入房间
     * @param message
     */
    private void handlerMessage(String message) {
        Map map = JSON.parseObject(message, Map.class);
        String eventName = (String) map.get("eventName");

        //p2p通信
        if (eventName.equals("__peers")) {
            handlerJoinRoom(map);
        }
    }

    /**
     * 连接至房间
     * @param map
     */
    private void handlerJoinRoom(Map map) {
        Map data = (Map) map.get("data");
        JSONArray array;
        if (data != null) {
            array = (JSONArray) data.get("connections");
            String js = JSONObject.toJSONString(array, SerializerFeature.WriteClassName);
            ArrayList<String> connections = (ArrayList<String>) JSONObject.parseArray(js, String.class);
            //获取自身唯一id
            String myId = (String) data.get("you");
            peerConnectionManager.joinToRoom(this,false, connections, myId);
        }
    }

    /**
     * 客户端向服务器发送请求参数 - 事件类型
     * 1.__join
     * 2.__answer
     * 3.__offer
     * 4.__ice_candidate
     * 5.__peer
     *
     * @param roomId
     */
    public void joinRoom(String roomId) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__join");
        Map<String, String> childMap = new HashMap<>();
        childMap.put("room", roomId);
        map.put("data", childMap);
        JSONObject jsonObject = new JSONObject(map);
        String jsonString = jsonObject.toJSONString();

        mWebSocketClient.send(jsonString);
    }

    /**
     * 忽略证书
     */
    public static class TrustManagerTest implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
