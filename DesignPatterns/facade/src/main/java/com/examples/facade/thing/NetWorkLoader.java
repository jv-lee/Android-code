package com.examples.facade.thing;

import java.io.InputStream;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public interface NetWorkLoader {

    /**
     * 从网络中获取图片文件流
     *
     * @param url 地址
     * @return stream
     */
    InputStream loadImageFromNet(String url);
}
