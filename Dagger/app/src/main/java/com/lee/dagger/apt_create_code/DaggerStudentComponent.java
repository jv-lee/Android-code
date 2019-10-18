package com.lee.dagger.apt_create_code;

import com.dagger.library.MembersInjector;
import com.dagger.library.Provider;
import com.lee.dagger.MainActivity;
import com.lee.dagger.Student;
import com.lee.dagger.StudentComponent;
import com.lee.dagger.StudentModule;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description APT 自动生成
 */
public class DaggerStudentComponent implements StudentComponent {

    public DaggerStudentComponent(Builder builder) {
        initialize(builder);
    }

    private Provider<Student> studentProvider;
    private MembersInjector<MainActivity> mainActivityMembersInjector;

    private void initialize(Builder builder) {
        studentProvider = StudentModule_ProviderStudentFactory.create(builder.studentModule);
        mainActivityMembersInjector = MainActivity_MembersInjector.create(studentProvider);
    }

    private static Builder builder() {
        return new Builder();
    }

    private final static class Builder {
        StudentModule studentModule;

        private Builder() {
        }

        public StudentComponent build() {
            if (studentModule == null) {
                studentModule = new StudentModule();
            }
            return new DaggerStudentComponent(this);
        }
    }

    /**
     * 对外提供的创建方法
     *
     * @return
     */
    public static StudentComponent create() {
        return builder().build();
    }

    @Override
    public void inject(MainActivity activity) {
        mainActivityMembersInjector.injectMembers(activity);
    }
}
