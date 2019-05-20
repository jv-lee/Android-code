package code.lee.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019/4/13
 * 申请权限时运行
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnPermission {

}
