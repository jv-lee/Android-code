package com.lee.hookproxy;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jv.lee
 * @date 2019-08-09
 * @description
 */
public class HookApplication extends Application {

    private static final String TAG = "HookApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            hookAmsAction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: hookAmsAction失败e:" + e.toString());
        }
    }

    /**
     * 要在执行AMS之前替换可用的Activity ，替换在AndroidManifest里面配置的Activity
     */
    private void hookAmsAction() throws Exception {
        //替换点

        //动态代理  本质是 IActivityManager
        Class mIActivityManagerClass = Class.forName("android.app.IActivityManager");

        //我们要拿到IActivityManager 对象，才能让动态代理里面的invoke正常执行下
        //执行此方法 static public IActivityManager getDefault(), 就能拿到IActivityManager
        Class mActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        final Object mIActivityManager = mActivityManagerNativeClass.getMethod("getDefault").invoke(null);


        Object mIActivityManagerProxy = Proxy.newProxyInstance(HookApplication.class.getClassLoader()
                , new Class[]{mIActivityManagerClass},
                new InvocationHandler() {

                    /**
                     * @param proxy 代理对象
                     * @param method IActivityManager 里面的方法
                     * @param args IActivityManager 里面的参数
                     * @return 返回方法体 继续执行
                     * @throws Throwable 异常
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("startActivity".equals(method.getName())) {
                            //做自己的业务逻辑
                        }

                        Log.d(TAG, "invoke: 拦截到了 IActivityManager里面的方法: " + method.getName());

                        //让系统正常往下执行
                        return method.invoke(mIActivityManager, args);
                    }
                });


        Class<?> mSingletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = mSingletonClass.getDeclaredField("mInstance");
        //让虚拟机不要检测权限修饰符
        mInstanceField.setAccessible(true);

        Class<?> mActivityManagerClass = Class.forName("android.app.ActivityManager");
        Field iActivityManagerSingletonField = mActivityManagerClass.getDeclaredField("IActivityManagerSingleton");
        iActivityManagerSingletonField.setAccessible(true);
        Object iActivityManagerSingleton = iActivityManagerSingletonField.get(null);

        mInstanceField.set(iActivityManagerSingleton,mIActivityManagerProxy);

        /**
         * 为了拿到gDefault
         * 通过 IActivityManagerNative 拿到 gDefault变量（对象）
         */
//        Field gDefaultField = mActivityManagerNativeClass.getDeclaredField("gDefault");
//        gDefaultField.setAccessible(true);
//        //静态方法无需传引用对象
//        Object gDefault = gDefaultField.get(null);
//
//
//        //替换是需要gDefault
//        mInstanceField.set(gDefault, mIActivityManagerProxy);
    }
}
