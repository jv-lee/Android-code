package com.lee.okhttprequset.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author jv.lee
 * @date 2019-08-31
 * @description 通过Socket请求服务器测试
 */
public class SocketConnectionTest {

    private static final String HTTP_PATH = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";
    private static final String HTTPS_PATH = "https://www.baidu.com";

    public static void main(String[] args) {
        try {
//            getSocketConnection(HTTPS_PATH);
            postSocketConnection(HTTP_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getSocketConnection(String path) throws IOException {
        Socket socket = null;

        URL url = new URL(path);
        String host = url.getHost();
        int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();

        if (url.getProtocol().equals("https")) {
            //SSL握手 访问HTTPS 的socket客户端
            socket = SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            //访问HTTP客户端
            socket = new Socket(host, port);
        }

        //TODO  写出：请求
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        /**HTTPS
         * GET / HTTP/1.1
         * Host: www.baidu.com
         */
        /**HTTP
         * GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1
         * Host: restapi.amap.com
         */
        bufferedWriter.write("GET /" + url.getFile() + " HTTP/1.1\r\n");
        bufferedWriter.write("Host: " + url.getHost() + "\r\n\r\n");
        bufferedWriter.flush();

        //TODO 读取：响应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String readLine = null;
            if ((readLine = bufferedReader.readLine()) != null) {
                System.out.println("响应数据：" + readLine);
            } else {
                break;
            }
        }
    }

    private static void postSocketConnection(String path) throws IOException {
        Socket socket = null;

        URL url = new URL(path);
        String host = url.getHost();
        int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();

        if (url.getProtocol().equals("https")) {
            //SSL握手 访问HTTPS 的socket客户端
            socket = SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            //访问HTTP客户端
            socket = new Socket(host, port);
        }

        //TODO  写出：请求
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        /**HTTPS
         * GET / HTTP/1.1
         * Host: www.baidu.com
         */
        /**HTTP
         * GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1
         * Host: restapi.amap.com
         * Content-Length: 48
         * Content-Type: application/x-www-form-urlencoded
         */
        bufferedWriter.write("POST /" + url.getFile() + " HTTP/1.1\r\n");
        bufferedWriter.write("Content-Length: " + url.getQuery().length() + "\r\n");
        bufferedWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");
        bufferedWriter.write("Host: " + url.getHost() + "\r\n\r\n");
        bufferedWriter.write(url.getQuery() + "\r\n");
        bufferedWriter.flush();

        //TODO 读取：响应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String readLine = null;
            if ((readLine = bufferedReader.readLine()) != null) {
                System.out.println("响应数据：" + readLine);
            } else {
                break;
            }
        }
    }
}
