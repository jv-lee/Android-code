package com.lee.dagger.apt_create_code;

import com.dagger.library.MembersInjector;
import com.dagger.library.Provider;
import com.lee.dagger.MainActivity;
import com.lee.dagger.Student;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description APT自动生成 完成注入操作 (MainActivity)目标 去注入Student对象的时候，在编译时期才会去生成
 */
public class MainActivity_MembersInjector implements MembersInjector<MainActivity> {

    private Provider<Student> studentProvider;

    public MainActivity_MembersInjector(Provider<Student> studentProvider) {
        this.studentProvider = studentProvider;
    }

    /**
     * 对外提供静态创建方法
     */
    public static MainActivity_MembersInjector create(Provider<Student> studentProvider) {
        return new MainActivity_MembersInjector(studentProvider);
    }

    @Override
    public void injectMembers(MainActivity instance) {
        if (null == instance) {
            throw new NullPointerException("inject target instance is null");
        }
        instance.student = studentProvider.get();
        instance.student2 = studentProvider.get();
    }
}
