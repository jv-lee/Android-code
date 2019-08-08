package com.lee.hookproxy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hook系统没有注册的Activity 测试
        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HookActivity.class));
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ((Button) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        try {
            hook(button);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "hook失败了", Toast.LENGTH_SHORT).show();
        }
    }

    private void hook(View view) throws Exception {

        //首先获取View类
        Class<?> mViewClass = Class.forName("android.view.View");
        //获取View中的 getListenerInfoMethod方法 会返回一个ListenerInfo对象 包含了 OnClickListener接口
        Method getListenerInfoMethod = mViewClass.getDeclaredMethod("getListenerInfo");
        getListenerInfoMethod.setAccessible(true);

        //使用当前view为执行对象 执行方法 获取内部的ListenerInfo对象
        Object mListenerInfo = getListenerInfoMethod.invoke(view);

        //首先获取mListenerInfoClass中的 click接口属性
        Class<?> mListenerInfoClass = Class.forName("android.view.View$ListenerInfo");
        Field mOnClickListenerField = mListenerInfoClass.getField("mOnClickListener");
        //属性通过 mListenerInfo对象来获取 至此已经获取到了 button的 OnClickListener接口对象
        final Object mOnClickListenerObject = mOnClickListenerField.get(mListenerInfo);

        //通过动态代理在方法中切面实现自己的逻辑
        Object mOnClickListenerProxy = Proxy.newProxyInstance(MainActivity.class.getClassLoader(),
                new Class[]{View.OnClickListener.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Log.i(TAG, "invoke: onclick");
                        Button button = new Button(MainActivity.this);
                        button.setText("拦截修改button.text");

                        return method.invoke(mOnClickListenerObject, button);
                    }
                });

        //开始替换 狸猫换太子
        mOnClickListenerField.set(mListenerInfo, mOnClickListenerProxy);
    }
}
