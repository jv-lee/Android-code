#include <jni.h>

int test(){
    return 123;
}

//com.lee.code.ndk
jint Java_com_lee_code_ndk_Jni_nativeTest(){
    return test();
}