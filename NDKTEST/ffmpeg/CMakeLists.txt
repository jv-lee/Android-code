cmake_minimum_required(VERSION 3.4.1)

#设置全局变量 编译所有cpp文件
file(GLOB SOURCE src/main/cpp/*.cpp)

add_library(
        my-player
        SHARED
        ${SOURCE})

find_library(
        log-lib
        log)

#引入头文件夹
include_directories(src/main/cpp/include)

#添加编译静态库变量
set(my_lib_path ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${my_lib_path}")

target_link_libraries(
        my-player
        avfilter avformat avcodec avutil swresample swscale
        ${log-lib}
        z
        OpenSLES
        android)