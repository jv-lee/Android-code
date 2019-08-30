package com.lee.okhttp.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.lee.okhttp.core.Request.POST;

/**
 * @author jv.lee
 * @date 2019-08-29
 * @description
 */
public class SocketRequestServer {

    private final String K = " ";
    private final String VERSION = "HTTP/1.1";
    private final String GRGN = "\r\n";

    /**
     * TODO 通过request对象 寻找到域名host
     *
     * @param request 请求参数封装对象
     * @return 服务器host地址
     */
    public String getHost(Request request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO 获取端口号
     *
     * @param request 请求封装对象
     * @return 端口号
     */
    public int getPort(Request request) {
        try {
            URL url = new URL(request.getUrl());
            int port = url.getPort();
            return port == -1 ? url.getDefaultPort() : port;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * TODO 获取请求头的所有信息
     *
     * @param request 请求封装对象
     * @return 请求题数据
     */
    public String getRequestHeaderAll(Request request) {
        //得到请求方式
        URL url = null;
        String file = null;
        try {
            url = new URL(request.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            file = url.getFile();
        }

        // TODO 请求头 - 请求行
        StringBuffer buffer = new StringBuffer();
        buffer.append(request.getRequestMethod())
                .append(K)
                .append(file)
                .append(K)
                .append(VERSION)
                .append(GRGN);

        // TODO 请求头 - 请求集
        if (!request.getHeaderList().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getHeaderList().entrySet()) {
                buffer.append(entry.getKey())
                        .append(":")
                        .append(K)
                        .append(entry.getValue())
                        .append(GRGN);
            }
            //拼接空行 请求头结束 后面开始是POST 请求体
            buffer.append(GRGN);
        }

        // TODO 拼接请求体  POST
        if (POST.equals(request.getRequestMethod())) {
            buffer.append(request.getRequestBody().getBody())
                    .append(GRGN);
        }
        return buffer.toString();
    }

}
