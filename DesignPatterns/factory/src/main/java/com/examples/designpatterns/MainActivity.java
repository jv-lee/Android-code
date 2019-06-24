package com.examples.designpatterns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.examples.designpatterns.api.Api;
import com.examples.designpatterns.factory.ParameterFactory;
import com.examples.designpatterns.factory.PropertiesFactory;
import com.examples.designpatterns.factory.SimpleFactory;

/**
 * @author jv.lee
 * 简单工厂模式
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Api api = new ApiImpl();
//        api.create();

        //简单工厂模式
//        Api api = SimpleFactory.create();
//        api.create();

        //参数工厂模式
//        Api api = ParameterFactory.createApi(2);
//        api.create();

        //配置文件工厂模式
        Api api = PropertiesFactory.createApi(this);
        if (api != null) {
            api.create();
        }
    }
}
