package com.lee.okhttp.chain;

import com.lee.okhttp.core.Call;
import com.lee.okhttp.core.Request;
import com.lee.okhttp.core.Response;

import java.io.IOException;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-29
 * @description 拦截器责任链管理类
 */
public class ChainManager implements Interceptor.Chain {

    private final List<Interceptor> interceptors;
    private final int index;
    private final Request request;
    private final Call call;

    public ChainManager(List<Interceptor> interceptors, int index, Request request, Call call) {
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

        ChainManager manager = new ChainManager(interceptors, index + 1, request, call);

        //取出第一个拦截器
        Interceptor interceptor = interceptors.get(index);
        Response response = interceptor.intercept(manager);

        return response;
    }
}
