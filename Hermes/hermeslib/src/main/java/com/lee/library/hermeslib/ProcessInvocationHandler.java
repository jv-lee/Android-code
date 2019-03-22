package com.lee.library.hermeslib;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 */
public class ProcessInvocationHandler implements InvocationHandler {
    private Class<?> clazz;
    private static final Gson gson = new Gson();
    public ProcessInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 在其他线程通过 ProcessManager.getInstance().getInstance(IUserManager.class) 获取到了代理对象 将执行代理对象的invoke方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //通过ProcessService onBind 获取方法时 最终返回的序列化json字符串数据
        Log.e("----- >>>> ", "invoke");
        String response = ProcessManager.getInstance().sendRequest(ProcessService.GET_METHOD, clazz, method, args);
        Log.e("------ >>>>", response == null ? "null" : response);
        if (!TextUtils.isEmpty(response) && !"null".equals(response)) {
            Class userClass = method.getReturnType();
            Object o = gson.fromJson(response, userClass);
            return o;
        }
        return null;
    }
}
