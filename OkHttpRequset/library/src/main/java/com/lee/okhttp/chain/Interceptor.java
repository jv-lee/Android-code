package com.lee.okhttp.chain;

import com.lee.okhttp.core.Request;
import com.lee.okhttp.core.Response;

import java.io.IOException;

/**
 * @author jv.lee
 * @date 2019-08-29
 * @description 拦截器
 */
public interface Interceptor {

    /**
     * 下一个责任链 链条任务
     *
     * @param chain
     * @return
     */
    Response intercept(Chain chain) throws IOException;

    interface Chain {
        Request request();

        Response proceed(Request request) throws IOException;
    }
}
