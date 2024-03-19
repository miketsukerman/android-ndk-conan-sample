cmake_minimum_required(VERSION 3.22.1)

project(myapp)

# include(${CMAKE_CURRENT_SOURCE_DIR}/../../../conan_build/${CMAKE_BUILD_TYPE}/${ANDROID_ABI}/conanbuildinfo.cmake)

# conan_basic_setup(TARGETS)

add_library(${CMAKE_PROJECT_NAME} SHARED
        native-lib.cpp)

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log)
