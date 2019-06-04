cmake_minimum_required(VERSION 3.4.1)

#使用C编译器编译  -D 传递参数  定义 NO-CRYPTO 不使用openssl 的宏定义
set(CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} -DNO_CRYPTO")

#设置C文件路径 编译成静态库
file(GLOB rtmp_source ./*.c)
add_library(
        rtmp
        STATIC
        ${rtmp_source}
)