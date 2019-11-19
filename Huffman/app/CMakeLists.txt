cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

include_directories(src/main/cpp/include)

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        jpeg
        turbojpeg
        jnigraphics
        ${log-lib})