package com.lee.library.hermeslib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lee.library.hermeslib.bean.RequestBean;
import com.lee.library.hermeslib.bean.RequestParameter;

import java.lang.reflect.InvocationTargetException;
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
                    //缓存UserManager对象 type `1
                    case GET_INSTANCE:
                        //获取UserManager对象
                        Method method = CacheCenter.getInstance().getMethod(requestBean.getClassName(), "getInstance");
                        //反射的前提
                        Object[] objects = makeParameterObject(requestBean);
                        try {
                            Object userManager = method.invoke(null, objects);
                            CacheCenter.getInstance().putObject(requestBean.getClassName(),userManager);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                        //获取UserManager对象  type 2
                    case GET_METHOD:
                        Object userManager = CacheCenter.getInstance().getObject(requestBean.getClassName());
                        Method getPerson = CacheCenter.getInstance().getMethod(requestBean.getClassName(), requestBean.getMethodName());
                        //反射获取Object数组
                        Object[] mParameters = makeParameterObject(requestBean);
                        try {
                            Object person = getPerson.invoke(userManager, mParameters);
                            return gson.toJson(person); //将Object数据 序列化为 json字符串 返回
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return null;
            }
        };
    }

    ////参数还原 使用json遍历字符串参数 通过反射 还原一个object 等于他原本的类型
    private Object[] makeParameterObject(RequestBean requestBean) {
        Object[] mParameters = null;
        RequestParameter[] requestParameters = requestBean.getRequestParameters();
        if (requestParameters != null && requestParameters.length > 0) {
            mParameters = new Object[requestParameters.length];
            for (int i = 0; i < requestParameters.length; i++) {
                RequestParameter requestParameter = requestParameters[i];
                Class clazz = CacheCenter.getInstance().getClassType(requestParameter.getParameterClassName());
                mParameters[i] = gson.fromJson(requestParameter.getParameterValue(), clazz);
            }
        }else{
            mParameters = new Object[0];
        }
        return mParameters;
    }
}
