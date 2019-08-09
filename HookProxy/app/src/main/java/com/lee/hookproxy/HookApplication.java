package com.lee.hookproxy;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-09
 * @description
 */
public class HookApplication extends Application {

    private static final String TAG = "HookApplication";
    private static final String INTENT_TAG = "action_intent";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            hookAmsAction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: hookAmsAction失败e:" + e.toString());
        }

        try {
            hookLauncherActivity();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: hookAmsAction失败e:" + e.toString());
        }


        try {
            pluginToApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 要在执行AMS之前替换可用的Activity ，替换在AndroidManifest里面配置的Activity
     */
    private void hookAmsAction() throws Exception {
        //替换点

        //动态代理  本质是 IActivityManager ActivityManager是 IActivityManager的实现类
        Class mIActivityManagerClass = Class.forName("android.app.IActivityManager");

        //我们要拿到IActivityManager 对象，才能让动态代理里面的invoke正常执行下
        //执行此方法 static public IActivityManager getDefault(), 就能拿到IActivityManager
        Class mActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        final Object mIActivityManager = mActivityManagerNativeClass.getMethod("getDefault").invoke(null);

        //代理监听当前方法
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
                            Intent intent = new Intent(HookApplication.this, ProxyActivity.class);
                            //把没有注册的保存
                            intent.putExtra(INTENT_TAG, (Intent) args[2]);
                            args[2] = intent;
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

        //android 8.0后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //8.0获取 IActivityManager对象的方法
            Class<?> mActivityManagerClass = Class.forName("android.app.ActivityManager");
            Field iActivityManagerSingletonField = mActivityManagerClass.getDeclaredField("IActivityManagerSingleton");
            iActivityManagerSingletonField.setAccessible(true);
            Object iActivityManagerSingleton = iActivityManagerSingletonField.get(null);

            //拿到当前的IActivityManager  通过 自己创建的代理IActivityManagerProxy替换
            mInstanceField.set(iActivityManagerSingleton, mIActivityManagerProxy);
        } else {
            /** android 8.0之前
             * 为了拿到gDefault
             * 通过 IActivityManagerNative 拿到 gDefault变量（对象）
             */
            Field gDefaultField = mActivityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            //静态方法无需传引用对象
            Object gDefault = gDefaultField.get(null);

            //替换是需要gDefault
            mInstanceField.set(gDefault, mIActivityManagerProxy);
        }

    }

    /**
     * 9.0以前适用
     * Hook LauncherActivity ActivityThread准备启动时 把TestActivity替换回去
     *
     * @throws Exception
     */
    private void hookLauncherActivity() throws Exception {
        /**
         * 替换步骤：
         * 1.拿到ActivityThread  -> public static ActivityThread currentActivityThread()
         * 2.再通过ActivityThread 拿到H（Handler）
         */
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object activityThreadObject = currentActivityThreadMethod.invoke(null);

        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        //获得到了ActivityThread中 真正的mH对象
        Handler mH = (Handler) mHField.get(activityThreadObject);

        //开始替换 mCallback
        Field mCallbackField = Handler.class.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        //替换系统的9.0以后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mCallbackField.set(mH, new MyCallback2());
        } else {
            mCallbackField.set(mH, new MyCallback());
        }

    }

    /**
     * 9.0之前
     */
    class MyCallback implements Handler.Callback {

        private final int LAUNCH_ACTIVITY = 100;

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what != LAUNCH_ACTIVITY) {
                return false;
            }
            try {
                //做我们自己的业务逻辑 （把proxyActivity替换回 TestActivity)
                Object obj = msg.obj;
                //获取之前hook携带过来的TestActivity
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);

                //获取intent对象，才能取出携带过来的标识
                Intent intent = (Intent) intentField.get(obj);
                Intent actionIntent = intent.getParcelableExtra(INTENT_TAG);

                if (actionIntent != null) {
                    //替换intent
                    intentField.set(obj, actionIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //让系统继续执行 返回false
            return false;
        }
    }

    /**
     * 9.0以后
     */
    class MyCallback2 implements Handler.Callback {

        private final int EXECUTE_TRANSACTION = 159;

        @Override
        public boolean handleMessage(Message msg) {
            /**
             * Handler的dispatchMessage有3个callback优先级，首先是msg自带的callback，其次是Handler的成员mCallback,最后才是Handler类自身的handlerMessage方法,
             * 它成员mCallback.handleMessage的返回值为true，则不会继续往下执行 Handler.handlerMessage
             * 我们这里只是要hook，插入逻辑，所以必须返回false，让Handler原本的handlerMessage能够执行.
             */

            //这是跳转的时候,要对intent进行还原
            if (msg.what == EXECUTE_TRANSACTION) {
                try {
                    //获取ClientTransaction 新版本事务处理机制 为了获取mActivityCallbacks
                    Class<?> mClientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                    Field mActivityCallbacksField = mClientTransactionClass.getDeclaredField("mActivityCallbacks");
                    mActivityCallbacksField.setAccessible(true);

                    //判断类型是否正确
                    if (!mClientTransactionClass.isInstance(msg.obj)) {
                        return true;
                    }

                    //当前msg.obj == ClientTransaction
                    Object mActivityCallbacksObj = mActivityCallbacksField.get(msg.obj);
                    //ClientTransaction List<ClientTransactionItem> mActivityCallbacks;
                    List list = (List) mActivityCallbacksObj;

                    //判断集合数量 获取当前的itemObj
                    if (list.size() == 0) {
                        return true;
                    }
                    Object mLaunchActivityItemObj = list.get(0);

                    //获取当前启动项
                    Class<?> mLaunchActivityItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
                    if (!mLaunchActivityItemClass.isInstance(mLaunchActivityItemObj)) {
                        return true;
                    }

                    //判断 LaunchActivityItemClass，
                    // 开始的 ActivityResultItem传入后被转换为 LaunchActivityItemClass
                    Field mIntentField = mLaunchActivityItemClass.getDeclaredField("mIntent");
                    mIntentField.setAccessible(true);

                    //获取intent 替换
                    Intent intent = (Intent) mIntentField.get(mLaunchActivityItemObj);
                    Intent actionIntent = (Intent) intent.getExtras().get(INTENT_TAG);

                    //用原来的intent 替换
                    mIntentField.set(mLaunchActivityItemObj, actionIntent);
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }


    /**
     * 把插件dexElement 和宿主 dexElement 融为一体
     *
     * @throws Exception
     */
    private void pluginToApplication() throws Exception {
        //1.找到dexElements 得到此对象

        //2.找到插件的 dexElements 得到此对象

        //3.创建出 新的 dexElements

        //4.宿主dexElements + 插件dexElements = 新的newDexElements

        //5.把新的newDexElements 设置到宿主中去
    }

}
