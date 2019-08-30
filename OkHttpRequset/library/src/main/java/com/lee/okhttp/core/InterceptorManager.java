package com.lee.okhttp.core;

import com.lee.okhttp.interceptor.Interceptor;

import java.io.IOException;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-29
 * @description 拦截器责任链管理类
 */
public class InterceptorManager implements Interceptor.Chain {

    private final List<Interceptor> interceptors;
    private final int index;
    private final Request request;
    private final Call call;

    public InterceptorManager(List<Interceptor> interceptors, int index, Request request, Call call) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
        this.call = call;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        //判断index++ 计数 不能大于 size 不能等于
        if (index >= interceptors.size()) {
            throw new AssertionError();
        }

        if (interceptors.isEmpty()) {
            throw new IOException("interceptors is empty");
        }

        //取出第一个拦截器 以此类推
        Interceptor interceptor = interceptors.get(index);

        InterceptorManager manager = new InterceptorManager(interceptors, index + 1, request, call);

        return interceptor.intercept(manager);
    }
}
