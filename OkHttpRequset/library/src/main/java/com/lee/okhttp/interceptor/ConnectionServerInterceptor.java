package com.lee.okhttp.interceptor;

import android.util.Log;

import com.lee.okhttp.core.Request;
import com.lee.okhttp.core.Response;
import com.lee.okhttp.core.SocketRequestServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jv.lee
 * @date 2019-08-30
 * @description 连接服务拦截器
 */
public class ConnectionServerInterceptor implements Interceptor {

    private static final String TAG = "OkHttp";

    @Override
    public Response intercept(Chain chain) throws IOException {
        SocketRequestServer srs = new SocketRequestServer();
        Request request = chain.request();

        //通过socket 连接服务器 获取请求数据
        Socket socket = new Socket(srs.getHost(request), srs.getPort(request));

        //TODO  请求 Output
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String requestHeader = srs.getRequestHeaderAll(request);
        Log.i(TAG, "intercept: " + requestHeader);

        bufferedWriter.write(requestHeader);
        bufferedWriter.flush();

        //TODO 响应 Input
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String readerLine = null;
                while (true) {
                    try {
                        if ((readerLine = bufferedReader.readLine()) != null) {
                            Log.i(TAG, "run: 服务器响应：" + readerLine);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Response response = new Response();

        //TODO 取出请求码
        String readLine = bufferedReader.readLine();
        //服务器响应的：HTTP/1.1 200 OK
        String[] strings = readLine.split(" ");
        response.code = Integer.parseInt(strings[1]);

        //TODO 取出响应体 body
        String bodyLine = null;
        try {
            while ((bodyLine = bufferedReader.readLine()) != null) {
                //读取空行 响应体
                if ("".equals(bodyLine)) {
                    response.body = bufferedReader.readLine();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
