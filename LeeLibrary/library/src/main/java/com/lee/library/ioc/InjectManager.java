package com.lee.library.ioc;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.EventBase;
import com.lee.library.ioc.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author jv.lee
 * description：注入管理器
 */
public class InjectManager {
    public static void inject(Activity activity) {
        //布局注入
        injectLayout(activity);
        //控件注入
        injectViews(activity);
        //事件注入
        injectEvents(activity);
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method method = aClass.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static View injectLayout(Fragment fragment) {
        Class<? extends Fragment> aClass = fragment.getClass();
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        View view = null;
        if (contentView != null) {
            int layoutId = contentView.value();
            //第一种方法
            view = LayoutInflater.from(fragment.getContext()).inflate(layoutId, null, false);
        }
        return view;
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
                    Object view = findViewById.invoke(activity, viewId);

                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectViews(Fragment fragment) {
        Class<? extends Fragment> aClass = fragment.getClass();
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
                    View view = Objects.requireNonNull(fragment.getView()).findViewById(viewId);

                    field.setAccessible(true);
                    field.set(fragment, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

                        //设置代理 获取监听实列
                        ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                        handler.addMethod(callBackListener, method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                        try {
                            //获取 R.id.xxx
                            Method valueMethod = annotationType.getDeclaredMethod("values");
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            for (int viewId : viewIds) {
                                View view = activity.findViewById(viewId);
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view, listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            //获取 mAdapter
                            Method valMethod = annotationType.getDeclaredMethod("value");
                            String value = (String) valMethod.invoke(annotation);

                            Field declaredField = aClass.getDeclaredField(value);
                            declaredField.setAccessible(true);
                            Method setter = declaredField.getType().getMethod(listenerSetter, listenerType);
                            setter.invoke(declaredField.get(activity), listener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public static void injectEvents(Fragment fragment) {
        Class<? extends Fragment> aClass = fragment.getClass();
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

                        //代理添加监听方法
                        ListenerInvocationHandler handler = new ListenerInvocationHandler(fragment);
                        handler.addMethod(callBackListener, method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                        try {
                            //获取 R.id.xxx
                            Method valueMethod = annotationType.getDeclaredMethod("values");
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            for (int viewId : viewIds) {
                                View view = Objects.requireNonNull(fragment.getView()).findViewById(viewId);
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view, listener);
                            }
                        } catch (Exception e) {
                        }

                        try {
                            //获取 mAdapter
                            Method valMethod = annotationType.getDeclaredMethod("value");
                            String value = (String) valMethod.invoke(annotation);

                            Field declaredField = aClass.getDeclaredField(value);
                            declaredField.setAccessible(true);
                            Method setter = declaredField.getType().getMethod(listenerSetter, listenerType);
                            setter.invoke(declaredField.get(fragment), listener);
                        } catch (Exception e) {
                        }

                    }
                }
            }
        }
    }


}
