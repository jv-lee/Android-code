package com.lee.library.aspect;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.lee.library.PermissionActivity;
import com.lee.library.annotation.Permission;
import com.lee.library.annotation.PermissionCancel;
import com.lee.library.annotation.PermissionDenied;
import com.lee.library.core.IPermission;
import com.lee.library.utils.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 权限申请的 方法环绕处理 Aspect
 */
@Aspect
public class PermissionAspect {

    /**
     * 切入点 该方法内部不做任何事情，只为了@Pointcut服务
     *
     * @param permission 权限
     */
    @Pointcut
            ("execution(@com.lee.library.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointActionMethod(Permission permission) {
    }

    /**
     * 对方法环绕坚挺
     */
    @Around("pointActionMethod(permission)")
    public void aProceedingJoinPoint(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        //小于6.0无需申请权限
        if (Build.VERSION.SDK_INT < 23) {
            point.proceed();
            return;
        }

        //定义一个上下文操作环境
        Context context = null;
        final Object thisObj = point.getThis();

        //context赋值
        if (thisObj instanceof Context) {
            context = (Context) thisObj;
        } else if (thisObj instanceof Fragment) {
            context = ((Fragment) thisObj).getActivity();
        }

        if (null == context || permission == null) {
            throw new IllegalAccessException("null == context || permission == null");
        }

        //可执行 调用权限处理的Activity 申请 检测权限处理
        final Context finalContext = context;
        PermissionActivity.requestPermissionAction(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void ganted() {
                //已获取到权限 让被 @Permission 的方法正常的执行下去
                try {
                    point.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() {
                //被轻微拒绝 调用被 @PermissionCancel注解的方法
                PermissionUtils.invokeAnnotation(thisObj, PermissionCancel.class);
            }

            @Override
            public void denied() {
                //被严重拒绝， 不再提醒; 调用被 @PermissionDenied注解的方法
                PermissionUtils.invokeAnnotation(thisObj, PermissionDenied.class);

                //不仅仅要提醒用户，还需要自动跳转到手机设置界面
                PermissionUtils.startAndroidSettings(finalContext);
            }
        });
    }

}
