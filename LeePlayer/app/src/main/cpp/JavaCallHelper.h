//
// Created by jv.lee on 2019/6/5.
//

#ifndef EXPLAY_JAVACALLHELPER_H
#define EXPLAY_JAVACALLHELPER_H

#include <jni.h>

class JavaCallHelper {
public:
    JavaCallHelper(JavaVM *_javaVM, JNIEnv *_env, jobject &_jobj);

    ~JavaCallHelper();

    void onPrepare(int thread);

    void onProgress(int thread, int progress);

    void onError(int thread, int code);

private:
    JavaVM *javaVM;
    JNIEnv *env;
    jobject jobj;
    jmethodID jmid_prepare;
    jmethodID jmid_progress;
    jmethodID jmid_error;
};


#endif //EXPLAY_JAVACALLHELPER_H
