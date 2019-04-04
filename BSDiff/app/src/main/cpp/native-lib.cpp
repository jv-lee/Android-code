#include <jni.h>
#include <string>

extern "C" {
    int x_main(int argc,const char * argv[]);
}

extern "C" JNIEXPORT jstring JNICALL
Java_code_lee_code_bsdiff_MainActivity_bsPatch(
        JNIEnv* env,
        jobject instance,
        jstring oldApk_,
        jstring patch_,
        jstring output_) {

    //将java字符串转换为C/C++的字符串， 转换为UTF-8格式的char指针
    const char *oldApk = env ->GetStringUTFChars(oldApk_,0);
    const char *patch = env ->GetStringUTFChars(patch_,0);
    const char *output = env ->GetStringUTFChars(output_,0);

    const char * argv[] = {"",oldApk,output,patch};
    x_main(4,argv);

    //释放指针 （java将变量/常量释放）  release之前已经调用了 close
    env->ReleaseStringUTFChars(oldApk_,oldApk);
    env->ReleaseStringUTFChars(patch_,patch);
    env->ReleaseStringUTFChars(output_,output);
    std::string hello = "Hello from C++";
        return env->NewStringUTF(hello.c_str());
}


