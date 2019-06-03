cmake_minimum_required(VERSION 3.4.1)

#引入子目录
add_subdirectory(src/main/cpp/librtmp)

#引入头文件
include_directories(src/main/cpp/include)
include_directories(src/main/cpp)

#添加源文件变量
file(GLOB native-source src/main/cpp/*.cpp)

#添加编译静态库变量
set(x264 ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${x264}")

add_library(
        native-lib
        SHARED
        ${native-source})

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        x264
        rtmp
        ${log-lib})