cmake_minimum_required(VERSION 3.4.1)

#引入头文件夹
include_directories(src/main/cpp/include/)

#添加编译静态库变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")

#设置全局变量 编译所有cpp文件
file(GLOB SOURCE src/main/cpp/*.cpp)

add_library(
        my-player
        SHARED
        ${SOURCE})

target_link_libraries(
        my-player
        avfilter avformat avcodec avutil swresample swscale
        log
        z
        OpenSLES
        android)