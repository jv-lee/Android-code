package com.lee.code.andfix;

import android.content.Context;
import android.util.Log;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author jv.lee
 * @date 2019-05-23
 */
public class DexManager {
    private Context context;

    public DexManager(Context context) {
        this.context = context;
    }

    public void load(File file) {
        try {
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(), new File(context.getCacheDir(), "opt").getAbsolutePath(), Context.MODE_PRIVATE);
            //当前dex中所有class 类名集合
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String clazzName = entries.nextElement();
                Log.i("tag", clazzName);
                Class realClazz = dexFile.loadClass(clazzName, context.getClassLoader());

                if (realClazz != null) {
                    Log.i("tag", "fixClazz");
                    fixClazz(realClazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换方法
     * @param realClazz
     */
    private void fixClazz(Class realClazz) {
        Method[] methods = realClazz.getMethods();
        for (Method method : methods) {
            Replace replace = method.getAnnotation(Replace.class);
            if (replace == null) {
                continue;
            }
            //获取网络下载正确的方法
            String clazz = replace.clazz();
            String methodName = replace.method();
            //本地bug方法
            try {
                Class cacheClass = Class.forName(clazz);
                Method errorMethod = cacheClass.getDeclaredMethod(methodName, method.getParameterTypes());
                Log.i("tag", "replace");
                replace(errorMethod, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public native static void replace(Method errorMethod, Method method);
}
