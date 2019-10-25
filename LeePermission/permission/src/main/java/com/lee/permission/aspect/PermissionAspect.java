package com.lee.permission.aspect;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;

import com.lee.permission.PermissionFragment;
import com.lee.permission.PermissionManager;
import com.lee.permission.annotation.Permission;
import com.lee.permission.annotation.PermissionCancel;
import com.lee.permission.annotation.PermissionDenied;
import com.lee.permission.core.IPermission;
import com.lee.permission.utils.PermissionUtils;

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
            ("execution(@com.lee.permission.annotation.Permission * *(..)) && @annotation(permission)")
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
        Activity context = null;
        final Object thisObj = point.getThis();

        //context赋值
        if (thisObj instanceof Activity) {
            context = (Activity) thisObj;
        } else if (thisObj instanceof Fragment) {
            context = ((Fragment) thisObj).getActivity();
        } else if (thisObj instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) thisObj).getActivity();
        }

        if (null == context || permission == null) {
            throw new IllegalAccessException("null == context || permission == null");
        }

        //可执行 调用权限处理的Activity 申请 检测权限处理
        final Activity finalContext = context;

        IPermission iPermission = new IPermission() {
            @Override
            public void granted() {
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
            }
        };

        PermissionFragment fragment = PermissionFragment.getPermissionFragment(permission.value(), permission.requestCode(), iPermission);
        FragmentManager manager = finalContext.getFragmentManager();
        PermissionManager.getInstance().bindFragment(manager,fragment);
    }

}
