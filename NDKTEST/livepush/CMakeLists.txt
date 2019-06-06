cmake_minimum_required(VERSION 3.4.1)

#源文件变量
file(GLOB lib_source src/main/cpp/*.cpp)

#引入子目录
add_subdirectory(src/main/cpp/librtmp)

#引入头文件夹
include_directories(src/main/cpp/include/)
include_directories(src/main/cpp/)

#添加编译静态库变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")

add_library(
        native-lib
        SHARED
        ${lib_source}
)

target_link_libraries(
        native-lib
        rtmp
        x264
        faac
        log)