package com.examples.designpatterns.factory;

import android.content.Context;

import com.examples.designpatterns.api.Api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class PropertiesFactory {

    public static Api createApi(Context context) {
        try {
            //加载配置文件
            Properties properties = new Properties();
            //如果放置在 app/src/main/assets 文件中
            InputStream inputStream = context.getAssets().open("config.properties");
            //如果放置在 app/src/main/res/raw 文件中
//        InputStream inputStream = context.getResources().openRawResource("config.properties");
            //Java的写法
//        InputStream inputStream = PropertiesFactory.class.getResourceAsStream("assets/config.properties");
            properties.load(inputStream);
            Class clazz = Class.forName(properties.getProperty("create_a"));
            return (Api) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
