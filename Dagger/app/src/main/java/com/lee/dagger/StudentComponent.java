package com.lee.dagger;

import com.dagger.library.annotation.Component;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description
 */
@Component(modules = StudentModule.class)
public interface StudentComponent {
    /**
     * 注入
     * @param activity
     */
    void inject(MainActivity activity);
}
