//
// Created by jv.lee on 2019/6/28.
//

#ifndef OPENPHOTO_MACRO_H
#define OPENPHOTO_MACRO_H

#include <android/log.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"LEE >>>",__VA_ARGS__)

#define DELETE(obj) if(obj){delete obj;obj = 0;}

#endif //OPENPHOTO_MACRO_H
