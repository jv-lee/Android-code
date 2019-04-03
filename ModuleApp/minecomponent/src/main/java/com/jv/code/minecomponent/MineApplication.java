package com.jv.code.minecomponent;

import android.app.Application;

import com.jv.code.componentlib.IComponentApp;
import com.jv.code.componentlib.ServiceFactory;

public class MineApplication extends Application implements IComponentApp {

    private static Application application;

    public static Application getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initializal(Application application) {
        this.application = application;
        ServiceFactory.getInstance().setmMineService(new MineService());
    }
}
