package com.lee.component;

import com.lee.component.order.Order_MainActivity;
import com.lee.component.personal.Personal_MainActivity;
import com.lee.library.RecordPathManager;
import com.lee.library.base.BaseApplication;

/**
 * @author jv.lee
 * @date 2019/7/10.
 * @description
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RecordPathManager.joinGroup("app", MainActivity.class.getSimpleName(), MainActivity.class);
        RecordPathManager.joinGroup("order", Order_MainActivity.class.getSimpleName(), Order_MainActivity.class);
        RecordPathManager.joinGroup("personal", Personal_MainActivity.class.getSimpleName(), Personal_MainActivity.class);
    }

}
