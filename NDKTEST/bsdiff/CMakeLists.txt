cmake_minimum_required(VERSION 3.4.1)

#查找文件系统中指定模式的路径：
file(GLOB bzip src/main/cpp/bzip/*.c)
#添加本地动态库，编译生成动态库
add_library(
        native-lib #模块名
        SHARED # 动态库
        #源文件
        src/main/cpp/native-lib.cpp
        ${bzip}
)
#查找系统库，日志输出库
find_library(
        log-lib
        log )
#需要链接或者编译的库
target_link_libraries(
        native-lib
        ${log-lib} )