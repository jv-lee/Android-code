package com.lee.library.mvp;


import androidx.lifecycle.LifecycleObserver;

import com.lee.library.utils.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public class MVPManager {

    public static void injectPresenter(BaseMvpActivity activity) {
        Class<? extends BaseMvpActivity> aClass = activity.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //获取属性上的注解
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                try {
                    //获取注解的值
                    Class<?> presenterClass = injectPresenter.value();
                    Object presenterObj = presenterClass.newInstance();
                    field.setAccessible(true);
                    field.set(activity, presenterObj);

                    //获取view注入
                    Method[] methods = presenterClass.getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("onAttachView")) {
                            method.invoke(presenterObj, activity);
                        }
                    }

                    activity.getLifecycle().addObserver((LifecycleObserver) presenterObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.getStackTraceString(e);
                }
            }
        }
    }

    public static void injectPresenter(BaseMvpFragment fragment) {
        Class<? extends BaseMvpFragment> aClass = fragment.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //获取属性上的注解
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                try {
                    //获取注解的值
                    Class<?> presenterClass = injectPresenter.value();
                    Object presenterObj = presenterClass.newInstance();
                    field.setAccessible(true);
                    field.set(fragment, presenterObj);

                    //获取view注入
                    Method[] methods = presenterClass.getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("onAttachView")) {
                            method.invoke(presenterObj, fragment);
                        }
                    }

                    fragment.getLifecycle().addObserver((LifecycleObserver) presenterObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.getStackTraceString(e);
                }
            }
        }
    }

}
