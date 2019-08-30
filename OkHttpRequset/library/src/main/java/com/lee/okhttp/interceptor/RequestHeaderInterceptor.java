package com.lee.okhttp.interceptor;

import com.lee.okhttp.core.InterceptorManager;
import com.lee.okhttp.core.RequestBody;
import com.lee.okhttp.core.Request;
import com.lee.okhttp.core.Response;
import com.lee.okhttp.core.SocketRequestServer;

import java.io.IOException;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019-08-30
 * @description 请求头拦截器
 */
public class RequestHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拼接请求头之 请求集
        InterceptorManager chainManager = (InterceptorManager) chain;
        Request request = chainManager.request();

        //设置请求头
        Map<String, String> headerList = request.getHeaderList();
        headerList.put("Host", new SocketRequestServer().getHost(request));

        if (Request.POST.equals(request.getRequestMethod())) {
            headerList.put("Content-Length", String.valueOf(request.getRequestBody().getBody().length()));
            headerList.put("Content-Type", RequestBody.TYPE);
        }

        //执行下一个拦截器
        return chain.proceed(request);
    }
}
