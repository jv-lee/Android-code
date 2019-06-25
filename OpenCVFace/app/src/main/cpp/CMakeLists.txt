cmake_minimum_required(VERSION 3.4.1)

#导入头文件
include_directories(include/)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}")
file(GLOB SOURCE *.cpp)

add_library(
        native-lib
        SHARED
        native-lib.cpp)

target_link_libraries(
        native-lib
        opencv_java4
        log
        android)