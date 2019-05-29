cmake_minimum_required(VERSION 3.4.1)

#引入子目录
add_subdirectory(src/main/cpp/librtmp)

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

find_library(
        log-lib
        log)


target_link_libraries(
        native-lib
        rtmp
        ${log-lib})