package com.lee.library.hermeslib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.lee.library.hermeslib.bean.RequestBean;

import java.lang.reflect.Method;

/**
 * 该服务为处理中心
 */
public class ProcessService extends Service {
    // type 1 获取对象
    public static final int GET_INSTANCE = 1;
    //type 2 执行方法
    public static final int GET_METHOD = 2;
    Gson gson = new Gson();

    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessInterface.Stub(){

            @Override
            public String send(String request) throws RemoteException {
                //反序列化为requestBean ProcessManager通过AIDL.send 请求回到主进程接受数据  其他进程 --->  回到主进程
                RequestBean requestBean = gson.fromJson(request, RequestBean.class);
                switch (requestBean.getType()) {
                    case GET_INSTANCE:
                        //获取UserManager对象
                        Method method = CacheCenter.getInstance().getMethod(requestBean.getClassName(), "getInstance");
                        method.invoke(null, );
                        break;
                    case GET_METHOD:
                        break;
                }
                return null;
            }
        };
    }
}
