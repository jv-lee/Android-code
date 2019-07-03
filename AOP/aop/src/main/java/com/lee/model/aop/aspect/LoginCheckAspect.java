package com.lee.model.aop.aspect;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.lee.model.aop.Config;
import com.lee.model.aop.LoginActivity;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author jv.lee
 * @date 2019-06-18
 * 定义切面类
 */
@Aspect
public class LoginCheckAspect {

    private final static String TAG = "lee >>>";

    /**
     * 1.应用中用到了哪些竹节，放到当前的切入点进行处理 （找到需要处理的切入点）
     * execution，以方法执行时作为切点，触发Aspect类
     * * *(..) 处理该类所有的方法
     */
    @Pointcut("execution(@com.lee.model.aop.annotation.LoginCheck * *(..))")
    public void methodPointCut() {
    }

    @Before("methodPointCut()")
    public void joinPointBefore(JoinPoint joinPoint){
        Log.i(TAG, "login before");
    }
    /**
     * 2.对切入点如何处理
     */
    @Around("methodPointCut()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context = (Context) joinPoint.getThis();
        //从SharedPreferences中读取
        if (Config.isLogin) {
            Log.e(TAG, "检测到已登陆");
            //已登陆后 继续执行方法
            return joinPoint.proceed();
        } else {
            Log.e(TAG, "检测到未登陆");
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            // 切入点的方法不执行 返回null
            return null;
        }
    }

    @After("methodPointCut()")
    public void joinPointAfter(JoinPoint joinPoint){
        Log.i(TAG, "login after");
    }

}
