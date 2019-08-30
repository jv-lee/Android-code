package com.lee.okhttp.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019-08-30
 * @description 请求体对象
 */
public class RequestBody {

    /**
     * 表单提交类型
     */
    public static final String TYPE = "application/x-www-form-urlencoded";

    private final String ENC = "utf-8";

    /**
     * 请求体集合 key=value&key=value
     */
    Map<String, String> bodys = new HashMap<>();

    /**
     * 添加请求体信息
     *
     * @param key
     * @param value
     */
    public void addBody(String key, String value) {
        try {
            bodys.put(URLEncoder.encode(key, ENC), URLEncoder.encode(value, ENC));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : bodys.entrySet()) {
            buffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (buffer.length() != 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }


}
