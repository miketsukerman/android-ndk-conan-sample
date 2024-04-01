cmake_minimum_required(VERSION 3.28)

project(myapp)

find_package(ZLIB REQUIRED CONFIG)

add_library(myapp SHARED
        native-lib.cpp)

target_link_libraries(myapp
        android
        log
        ZLIB::ZLIB
        )
