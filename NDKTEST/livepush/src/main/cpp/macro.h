//
// Created by jv.lee on 2019/5/31.
//

#ifndef NDKTEST_MACRO_H
#define NDKTEST_MACRO_H

#include <android/log.h>
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"LEE>>>",__VA_ARGS__)
//宏函数
#define DELETE(obj) if(obj){ delete obj; obj = 0; }

#endif //NDKTEST_MACRO_H
