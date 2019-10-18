package com.lee.dagger.apt_create_code;

import com.dagger.library.Factory;
import com.lee.dagger.Student;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description 默契编译时期 APT  自动生成
 */
public enum Student_Factory implements Factory<Student> {

    /**
     * 实例
     */
    INSTANCE;

    @Override
    public Student get() {
        return new Student();
    }

    public static Factory create() {
        return INSTANCE;
    }
}
