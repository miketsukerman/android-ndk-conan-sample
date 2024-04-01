#include <jni.h>
#include <string>
#include "zlib.h"

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_myapp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++, zlib version: ";
    hello.append(zlibVersion());
    return env->NewStringUTF(hello.c_str());
}
