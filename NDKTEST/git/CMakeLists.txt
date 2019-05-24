cmake_minimum_required(VERSION 3.4.1)

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp
        src/main/cpp/openbsd-reallocarray.c
        src/main/cpp/dgif_lib.c
        src/main/cpp/gifalloc.c)

find_library(
        log-lib
        log)

find_library(
        jnigraphics-lib
        jnigraphics)

target_link_libraries(
        native-lib
        ${jnigraphics-lib}
        ${log-lib})