package com.examples.facade.impl;

import com.examples.facade.thing.NetWorkLoader;

import java.io.InputStream;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class NetWorkLoaderImpl implements NetWorkLoader {
    @Override
    public InputStream loadImageFromNet(String url) {
        System.out.println("通过图片url，从网络加载图片");
        return null;
    }
}
