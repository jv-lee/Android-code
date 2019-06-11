cmake_minimum_required(VERSION 3.4.1)

#引入头文件
include_directories(src/main/cpp/include/)

#添加静态库编译 为系统库
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/cpp/libs/${ANDROID_ABI}")

#添加cpp文件编译库变量
file(GLOB SOURCE src/main/cpp/*.cpp)

#添加编译本地动态库
add_library(
        player
        SHARED
        ${SOURCE})

target_link_libraries(
        player
        avfilter avformat avcodec avutil swresample swscale
        log
        z
        OpenSLES
        android)