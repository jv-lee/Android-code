package com.lee.dagger;

import com.dagger.library.annotation.Module;
import com.dagger.library.annotation.Providers;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description
 */
@Module
public class StudentModule {

    @Providers
    public Student providerStudent() {
        return new Student();
    }
}
