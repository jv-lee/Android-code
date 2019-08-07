package com.lee.plugin.placeholder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description
 */
public class PluginManager {

    private static final String TAG = "PluginManager";

    private static PluginManager instance;

    private Context context;

    private PluginManager() {
    }

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    private DexClassLoader dexClassLoader;
    private Resources resources;


    public void loadPlugin() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
            if (!file.exists()) {
                Log.d(TAG, "loadPlugin: 插件包不存在...");
                return;
            }

            //插件包路径
            String pluginPath = file.getAbsolutePath();
            //插件包代码缓存路径
            File fileDir = context.getDir("pDir", Context.MODE_PRIVATE);

            //创建dexClassLoader 用于加载插件中的Activity.class
            dexClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, context.getClassLoader());

            //加载插件包的路径 获取插件包中的 资源数据
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);

            //创建Resources 获取插件中的Resources
            Resources r = context.getResources();
            resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射系统源码，来解析apk文件里面的所有信息
     */
    public void parserApkAction() {
        try {
            //插件包
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");

            //实例化 PackageParser对象
            Class<?> mPackageParserClass = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser = mPackageParserClass.newInstance();

            //执行此方法 public Package parsePackage(File packageFile,int flags) 拿到Package
            Method mPackageParserMethod = mPackageParserClass.getMethod("parsePackage", File.class, int.class);

            //获取Package类
            Object mPackage = mPackageParserMethod.invoke(mPackageParser, file, PackageManager.GET_ACTIVITIES);

            //获取得到receivers集合
            Field receiversField = mPackage.getClass().getDeclaredField("receivers");
            Object receivers = receiversField.get(mPackage);
            ArrayList array = (ArrayList) receivers;

            //同名类 非组件 - 是PackageParser里的内部类  mActivity-> Receiver
            for (Object mActivity : array) {
                //获取intent-filter  intents == Intent-Filter
                Class mComponentClass = Class.forName("android.content.pm.PackageParser$Component");
                Field intentsField = mComponentClass.getDeclaredField("intents");
                ArrayList<IntentFilter> intents = (ArrayList) intentsField.get(mActivity);

                //generateActivityInfoMethod方法参数
                Class<?> mPackageUserState = Class.forName("android.content.pm.PackageUserState");

                Class<?> mUserHandler = Class.forName("android.os.UserHandle");

                //静态方法无需对象
                int userId = (int) mUserHandler.getMethod("getCallingUserId").invoke(null);

                /**
                 * 拿到 android:name=".StaticReceiver"  -> activityInfo.name == android:name=".StaticReceiver"
                 * 执行此方法就能拿到ActivityInfo
                 * public static final ActivityInfo generateActivityInfo(Activity a,int flags,PackageUserState state,int userId)
                 */
                Method generateActivityInfoMethod = mPackageParserClass.getMethod("generateActivityInfo", mActivity.getClass(), int.class, mPackageUserState, int.class);

                //活得ActivityInfo实例
                ActivityInfo mActivityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null, mActivity, 0, mPackageUserState.newInstance(), userId);

                //获取静态广播全类名
                String receiverClassName = mActivityInfo.name;

                //加载静态广播class
                Class<?> mStaticReceiver = getDexClassLoader().loadClass(receiverClassName);

                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) mStaticReceiver.newInstance();

                for (IntentFilter intentFilter : intents) {
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }
}
