import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

@Throws(RuntimeException::class)
fun Project.runConan(buildType: String, arch: String) = ByteArrayOutputStream().use { outputStream ->
    exec {
        standardOutput = outputStream
        workingDir = projectDir

        commandLine("conan","install"
            ,"src/main/cpp", "--profile","android", "-s", "build_type=$buildType", "-s", "arch=$arch",
            "--build", "missing", "-c", "tools.cmake.cmake_layout:build_folder_vars=['settings.arch']")

    }.let { result ->
        when {
            result.exitValue != 0 -> throw RuntimeException("Conan failed.")
            else -> outputStream.toString()
                .trim()
                .let { "Conan output:\n$it" }
                .let(::println)
        }
    }
}

// https://docs.conan.io/2.0/examples/cross_build/android/android_studio.html
tasks.register("conanInstall") {
    listOf<String>("Debug","Release", "RelWithDebInfo", "MinSizeRel").forEach {buildType ->
        listOf<String>("armv7", "armv8", "x86", "x86_64").forEach{arch ->
            runConan(buildType, arch)
        }
    }
}

android {
    namespace = "com.example.myapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                cppFlags += "-v"
                arguments += "-DCMAKE_TOOLCHAIN_FILE=conan_android_toolchain.cmake"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}