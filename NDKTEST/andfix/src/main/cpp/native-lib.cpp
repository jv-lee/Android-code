//
// Created by jv.lee on 2019-05-23.
//
#include <jni.h>
#include <string>
#include "art_method.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_code_andfix_DexManager_replace(JNIEnv *env, jclass type, jobject errorMethod,
                                            jobject method) {

    //获取错误的方法结构体  art
    art::mirror::ArtMethod *errorArt = (art::mirror::ArtMethod *) env->FromReflectedMethod(errorMethod);
    //获取修复的方法结构体
    art::mirror::ArtMethod *updateArt = (art::mirror::ArtMethod *) env->FromReflectedMethod(method);


    errorArt->declaring_class_ = updateArt->declaring_class_;
    errorArt->dex_cache_resolved_methods_ = updateArt->dex_cache_resolved_methods_;

    errorArt->access_flags_ = updateArt->access_flags_;
    errorArt->dex_cache_resolved_types_ = updateArt->dex_cache_resolved_types_;
    errorArt->dex_code_item_offset_ = updateArt->dex_code_item_offset_;
    //方法索引替换
    errorArt->method_index_ = updateArt->method_index_;
    errorArt->dex_method_index_ = updateArt->dex_method_index_;

}