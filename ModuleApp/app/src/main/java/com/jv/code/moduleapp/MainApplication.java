package com.jv.code.moduleapp;

import android.app.Application;

import com.jv.code.componentlib.AppConfig;
import com.jv.code.componentlib.IComponentApp;

public class MainApplication extends Application implements IComponentApp {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initializal(this);
    }

    @Override
    public void initializal(Application application) {
        for (String component : AppConfig.COMPONENTS) {
            try {
                //通过配置文件中的Application的路径，实例化，并将MainApp的application对象传过去
                Class<?> clazz = Class.forName(component);
                Object object = clazz.newInstance();
                if (object instanceof IComponentApp) {
                    ((IComponentApp) object).initializal(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
