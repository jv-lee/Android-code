package com.lee.okhttprequset.connection;

import android.text.TextUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 * @date 2019-08-31
 * @description 连接对象
 */
public class HttpConnection {

    private static final String TAG = "HttpConnection";

    Socket socket;

    /**
     * 连接对象最后使用的时间
     */
    long lastUseTime;

    public HttpConnection(final String host, final int port) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isConnectionAction(String host, int port) {
        if (socket == null) {
            return false;
        }

        try {
            if (socket.getInetAddress().getHostName().equalsIgnoreCase(host) && socket.getPort() == port) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 关闭socket
     */
    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
