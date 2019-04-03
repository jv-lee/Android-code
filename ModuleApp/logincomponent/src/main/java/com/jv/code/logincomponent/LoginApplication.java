package com.jv.code.logincomponent;

import android.app.Application;

import com.jv.code.componentlib.IComponentApp;
import com.jv.code.componentlib.ServiceFactory;

public class LoginApplication extends Application implements IComponentApp {
    private static Application application;

    public static Application getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public void initializal(Application application) {
        this.application = application;
        ServiceFactory.getInstance().setmLoginService(new LoginService());
    }
}
