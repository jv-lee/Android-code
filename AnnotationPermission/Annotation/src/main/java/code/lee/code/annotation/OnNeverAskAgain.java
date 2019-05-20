package code.lee.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jv.lee
 * @date 2019/4/13
 * 用户选择不在讯问后提示
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface OnNeverAskAgain {

}
