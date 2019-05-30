cmake_minimum_required(VERSION 3.4.1)

#引入子目录
add_subdirectory(src/main/cpp/librtmp)

#引入头文件夹
include_directories(src/main/cpp/include)

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

#添加编译静态库变量
set(x264 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${x264}")

find_library(
        log-lib
        log)


target_link_libraries(
        native-lib
        rtmp
        ${log-lib}
        x264)