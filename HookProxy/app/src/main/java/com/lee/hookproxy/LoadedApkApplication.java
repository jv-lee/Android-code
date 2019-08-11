package com.lee.hookproxy;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lee.hookproxy.loaded.PluginClassLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019-08-09
 * @description loadedApk式插件化application
 */
public class LoadedApkApplication extends Application {

    private static final String TAG = "LoadedApkApplication";
    private static final String INTENT_TAG = "action_intent";

    /**
     * 插件包文件
     */
    private File pluginFile;
    /**
     * 插件包文件绝对路径
     */
    private String pluginPath;
    /**
     * 插件包class缓存地址
     */
    private String pluginCachePath;

    @Override
    public void onCreate() {
        super.onCreate();
        readPluginFile();
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
            //自定义LoadedApk的方式
            customLoadedApkAction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: customLoadedApkAction失败e:" + e.toString());
        }

    }

    /**
     * 预读插件
     */
    private void readPluginFile() {
        pluginFile = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin-debug.apk");
        pluginPath = pluginFile.getAbsolutePath();
        pluginCachePath = getDir("pluginDir", MODE_PRIVATE).getAbsolutePath();
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
        Object mIActivityManagerProxy = Proxy.newProxyInstance(LoadedApkApplication.class.getClassLoader()
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
                            Intent intent = new Intent(LoadedApkApplication.this, ProxyActivity.class);
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
                //做我们自己的业务逻辑 （把proxyActivity替换回 TestActivity)  obj == ActivityClientRecord（activity记录）
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

                    //在以下代码中做插件和宿主区分 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    Field activityInfoField = obj.getClass().getDeclaredField("activityInfo");
                    activityInfoField.setAccessible(true);
                    ActivityInfo activityInfo = (ActivityInfo) activityInfoField.get(obj);

                    //判断加载插件时机和宿主 actionIntent为插件
                    if (actionIntent.getPackage() == null) {
                        //插件
                        activityInfo.applicationInfo.packageName = actionIntent.getComponent().getPackageName();

                        // Hook 拦截此 getPackageInfo 做自己的逻辑
                        hookGetPackageInfo();
                    } else {
                        //宿主
                        activityInfo.applicationInfo.packageName = actionIntent.getPackage();
                    }

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
                    Intent actionIntent = intent.getParcelableExtra(INTENT_TAG);
                    if (actionIntent != null) {
                        //用原来的intent 替换
                        mIntentField.set(mLaunchActivityItemObj, actionIntent);

                        //在以下代码中做插件和宿主区分 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        Field activityInfoField = mLaunchActivityItemClass.getDeclaredField("mInfo");
                        activityInfoField.setAccessible(true);
                        ActivityInfo activityInfo = (ActivityInfo) activityInfoField.get(mLaunchActivityItemObj);

                        //判断加载插件时机和宿主 actionIntent为插件
                        if (actionIntent.getPackage() == null) {
                            //插件
                            activityInfo.applicationInfo.packageName = actionIntent.getComponent().getPackageName();

                            hookGetPackageInfo();
                        } else {
                            //宿主
                            activityInfo.applicationInfo.packageName = actionIntent.getPackage();
                        }
                    }

                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * 自己创建一个LoadedApk.ClassLoader 添加到 mPackages,此LoadedApk 专门用来加载插件里面的class
     */
    private void customLoadedApkAction() throws Exception {
        if (!pluginFile.exists()) {
            throw new FileNotFoundException("插件包不存在");
        }
        //mPackages 添加自定义的LoadedApk
        //final ArrayMap<String,WeakReference<LoadedApk>> mPackages 添加自定义LoadedApk
        Class<?> mActivityThreadClass = Class.forName("android.app.ActivityThread");

        //执行此方法 public static ActivityThread currentActivityThread() 拿到ActivityThread对象
        Object mActivityThreadObj = mActivityThreadClass.getMethod("currentActivityThread").invoke(null);
        //获取mPackages属性
        Field mPackagesField = mActivityThreadClass.getDeclaredField("mPackages");
        mPackagesField.setAccessible(true);

        //拿到mPackages对象
        Object mPackagesObj = mPackagesField.get(mActivityThreadObj);

        //强转实质类型
        Map mPackages = (Map) mPackagesObj;

        //如何自定义LoadedApk
        //执行此方法  public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai,CompatibilityInfo compatInfo)
        Class<?> mCompatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
        //该属性直接拿到CompatibilityInfo实例
        Field mDefaultCompatibilityInfoField = mCompatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
        mDefaultCompatibilityInfoField.setAccessible(true);
        //获得CompatibilityInfo实例
        Object mCompatibilityInfoObj = mDefaultCompatibilityInfoField.get(null);

        //ApplicationInfo 如何获取
        ApplicationInfo applicationInfo = getApplicationInfoAction();

        //执行拿到 LoadedApk对象 成功创建自定义的loadedApk
        Method mLoadedApkMethod = mActivityThreadClass.getMethod("getPackageInfoNoCheck", ApplicationInfo.class, mCompatibilityInfoClass);
        Object mLoadedApkObj = mLoadedApkMethod.invoke(mActivityThreadObj, applicationInfo, mCompatibilityInfoObj);

        //添加自定义的LoadedApk 专门加载插件中的Class  自定义ClassLoader
        ClassLoader classLoader = new PluginClassLoader(pluginPath, pluginCachePath, null, getClassLoader());
        Field mClassLoaderField = mLoadedApkObj.getClass().getDeclaredField("mClassLoader");
        mClassLoaderField.setAccessible(true);
        //替换loaderApk里面的ClassLoader
        mClassLoaderField.set(mLoadedApkObj, classLoader);

        //最终的目标
        WeakReference weakReference = new WeakReference(mLoadedApkObj);
        mPackages.put(applicationInfo.packageName, weakReference);
    }

    /**
     * 获得ApplicationInfo 为插件服务的
     * getPackageInfoNoCheck 第一个参数
     *
     * @return
     * @throws Exception
     */
    private ApplicationInfo getApplicationInfoAction() throws Exception {
        //执行此方法 public static ApplicationInfo generateApplicationInfo(Package p, int flags,PackageUserState state) {
        Class<?> mPackageParserClass = Class.forName("android.content.pm.PackageParser");
        Object mPackageParserObj = mPackageParserClass.newInstance();

        //generateApplicationInfo方法 参数类型
        Class<?> $packageClass = Class.forName("android.content.pm.PackageParser$Package");
        Class<?> mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");

        Method mGenerateApplicationInfoMethod = mPackageParserClass.getMethod("generateApplicationInfo", $packageClass, int.class, mPackageUserStateClass);

        //执行此public Package parsePackage(File packageFile, int flags) 方法 ，拿到内部类的 Package
        Method mParsePackageMethod = mPackageParserClass.getMethod("parsePackage", File.class, int.class);
        Object mPackageObj = mParsePackageMethod.invoke(mPackageParserObj, pluginFile, PackageManager.GET_ACTIVITIES);

        //获得插件的applicationInfo
        ApplicationInfo applicationInfo = (ApplicationInfo) mGenerateApplicationInfoMethod.invoke(mPackageParserObj, mPackageObj, 0, mPackageUserStateClass.newInstance());

        //插件的applicationInfo 设置插件路径
        applicationInfo.publicSourceDir = pluginPath;
        applicationInfo.sourceDir = pluginPath;

        return applicationInfo;
    }


    /**
     * Hook 拦截此 getPackageInfo 方法
     */
    private void hookGetPackageInfo() {
        try {
            // sPackageManager 替换成我们自己的动态代理
            Class<?> mActivityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = mActivityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);

            Field sPackageManagerField = mActivityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            //静态参数
            final Object mPackageManagerObj = sPackageManagerField.get(null);

            /**
             * 动态代理sPackageManager
             * 被监听的接口 IPackageManager
             */
            Class<?> mIPackageManagerClass = Class.forName("android.content.pm.IPackageManager");
            Object mIPackageManagerProxy = Proxy.newProxyInstance(getClassLoader(),
                    new Class[]{mIPackageManagerClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("getPackageInfo".equals(method.getName())) {
                                //pi != null
                                return new PackageInfo();
                            }

                            //让系统继续执行
                            return method.invoke(mPackageManagerObj, args);
                        }
                    });

            //替换 换成自己的动态代理 静态无需传引入对象
            sPackageManagerField.set(null, mIPackageManagerProxy);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "hookGetPackageInfo: hook失败");
        }
    }

}
