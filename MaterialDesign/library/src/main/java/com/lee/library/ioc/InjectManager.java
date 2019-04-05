package com.lee.library.ioc;

import android.app.Activity;
import android.view.View;

import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.EventBase;
import com.lee.library.ioc.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {
    public static void inject(Activity activity) {
        //布局注入
        injectLayout(activity);
        //控件注入
        injectViews(activity);
        //事件注入
        injectEvents(activity);
    }

    public static void injectEvents(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //获取每个方法的注解 可能又多个注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取OnClick注解上的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                    //事件的3个重要成员
                    String listenerSetter = eventBase.listenerSetter();
                    Class<?> listenerType = eventBase.listenerType();
                    String callBackListener = eventBase.callBackListener();

                    try {
                        //获取 R.id.xxx
                        Method valueMethod = annotationType.getDeclaredMethod("values");
                        int[] viewIds = (int[]) valueMethod.invoke(annotation);

                        ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                        handler.addMethod(callBackListener,method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType},handler);
                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            setter.invoke(view, listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        }
    }

    private static void injectViews(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        //获取类的所有属性
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //获取属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                //获取注解的值
                int viewId = injectView.value();

                //获取方法
                try {
                    Method findViewById = aClass.getMethod("findViewById", int.class);
                    //执行方法
                    Object view = findViewById.invoke(activity, viewId);
                    //第一种写法
//                    view = activity.findViewById(viewId);

                    //第二种写法 修改属性值为私有 赋值错误
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //执行方法
            }
        }
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            //第一种方法
            activity.setContentView(layoutId);
            try {
                Method method = aClass.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
