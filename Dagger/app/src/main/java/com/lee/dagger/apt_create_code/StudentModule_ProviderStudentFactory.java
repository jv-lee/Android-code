package com.lee.dagger.apt_create_code;

import com.dagger.library.Factory;
import com.dagger.library.Preconditions;
import com.lee.dagger.Student;
import com.lee.dagger.StudentModule;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description APT 自动生成
 */
public class StudentModule_ProviderStudentFactory implements Factory<Student> {

    private StudentModule studentModule;

    public StudentModule_ProviderStudentFactory(StudentModule studentModule) {
        this.studentModule = studentModule;
    }

    @Override
    public Student get() {
        return Preconditions.checkNotNull(studentModule.providerStudent(),
                "studentModule.providerStudent() is null exception...");
    }

    public static Factory<Student> create(StudentModule studentModule) {
        return new StudentModule_ProviderStudentFactory(studentModule);
    }

}
