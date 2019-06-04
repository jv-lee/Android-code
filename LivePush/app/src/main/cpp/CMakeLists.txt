#cmake_minimum_required(VERSION 3.4.1)
#
##引入头文件
#include_directories(./include)
#
##引入子目录
#add_subdirectory(./librtmp)
#
##设置当前目录源文件变量
#file(GLOB lib_source ./*.cpp)
#
##添加静态库编译
#set(x264 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${x264}")
#
#add_library(
#        native-lib
#        SHARED
#        ${lib_source})
#
#find_library(
#        log-lib
#        log)
#
#target_link_libraries(
#        native-lib
#        rtmp
#        x264
#        ${log-lib})




cmake_minimum_required(VERSION 3.4.1)

#引入子目录
add_subdirectory(./librtmp)
file(GLOB lib_source ./*.cpp)
add_library(
        native-lib
        SHARED
        ${lib_source}
)

#引入头文件夹
include_directories(./include)
include_directories(./cpp)

#添加编译静态库变量
set(x264 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${x264}")

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        rtmp
        x264
        ${log-lib})