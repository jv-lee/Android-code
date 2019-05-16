#定义模块当前路径
LOCAL_PATH := $(call my-dir)

#清空当前环境变量
include $(CLEAR_VARS)

#定义当前模块名
LOCAL_MODULE := hello-jni

#定义当前模块包含的源代码文件 多个文件使用空格隔开
LOCAL_SRC_FILES := hello-jni.c

#生成一个动态库
include $(BUILD_SHARED_LIBRARY)